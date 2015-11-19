/**
 * A C source file for rrd.RRDJNI class.  This class will acces the
 * RRD library.
 *
 * author:  Dave Neitz - Sprint E|Solutions, February 2002
 */

#include "RRDJNI.h"
#include <time.h>
#include <rrd.h>
#include <stdlib.h>

static char ** initArgs(JNIEnv *env, jobjectArray argv, char *cmd);
static void freeArgs(JNIEnv *env, jobjectArray argv, char *argv2[]);
static void throwException(JNIEnv *env, char *exception, char *msg);

JNIEXPORT void JNICALL Java_gnu_rrd_RRDJNI_create
(JNIEnv *env, jclass cl, jobjectArray argv) {
  char **argv2;

  argv2 = initArgs(env, argv, "create");
  rrd_create((*env)->GetArrayLength(env, argv)+1, argv2);
  freeArgs(env, argv, argv2);
  if (rrd_test_error())
    throwException(env, "gnu/rrd/RRDException", rrd_get_error());
}

JNIEXPORT void JNICALL Java_gnu_rrd_RRDJNI_update
(JNIEnv *env, jclass cl, jobjectArray argv) {
  char **argv2;
    
  argv2 = initArgs(env, argv, "update");
  rrd_update((*env)->GetArrayLength(env, argv)+1, argv2);
  freeArgs(env, argv, argv2);
  if (rrd_test_error())
    throwException(env, "gnu/rrd/RRDException", rrd_get_error());
}

JNIEXPORT jobject JNICALL Java_gnu_rrd_RRDJNI_graph
(JNIEnv *env, jclass cl, jobjectArray argv) {
  char        **argv2;
  char        **calcpr;
  char*       info;
  const char* fn;
  int         asize = 0;
  int         i, j, xsize,  ysize;
  double      ymin, ymax;
  jcharArray  graph;
  jclass      RRDGraphClass;
  jmethodID   RRDGraphMethodInit;
  jmethodID   RRDGraphMethodSetElement;
  jobject     graphObj = NULL;
  jobject     o;

  o  = (*env)->GetObjectArrayElement(env, argv, 0);  
  fn = (*env)->GetStringUTFChars(env, (jstring)o, NULL);
  if (strcmp(fn,"-")==0) {
    throwException(env, "gnu/rrd/RRDException", "output to stdout is not supported.");
    return;
  }
  
  RRDGraphClass      = (*env)->FindClass(env, "gnu/rrd/RRDGraph");
  RRDGraphMethodInit = (*env)->GetMethodID(env, RRDGraphClass, "<init>",
					   "(Ljava/lang/String;Ljava/lang/String;IIDD)V");
    
  calcpr = NULL;
  argv2  = initArgs(env, argv, "graph");
  if (rrd_graph((*env)->GetArrayLength(env, argv)+1, argv2, &calcpr, &xsize, &ysize, NULL, &ymin, &ymax) != -1 ) {
    
    if (calcpr) {
      for(i = 0; calcpr[i]; i++) asize = asize + strlen(calcpr[i]) + 1;
      if((info = (char*)malloc(asize+1))==NULL) {
	throwException(env, "gnu/rrd/RRDException", "unable to allocate memory for graph information.");
	freeArgs(env, argv, argv2);
	free(calcpr);
        return;
      } else {
	for(i = 0; calcpr[i]; i++){
	  if (i>0) info = strcat(strcat(info, "\n"), calcpr[i]);
	  else     info = strcat(info, calcpr[i]);
          free(calcpr[i]);
        } 
      }
      free(calcpr);
    }
    graphObj = (*env)->NewObject(env, RRDGraphClass, RRDGraphMethodInit,
				 (jstring)(*env)->GetObjectArrayElement(env, argv, 0),
				 (jstring)(*env)->NewStringUTF(env, info),
				 (jint)xsize, (jint)ysize, (jdouble)ymin, (jdouble)ymax);
  }
  freeArgs(env, argv, argv2);
  if (rrd_test_error())
    throwException(env, "gnu/rrd/RRDException", rrd_get_error());

  return graphObj;
}

JNIEXPORT void JNICALL Java_gnu_rrd_RRDJNI_dump
(JNIEnv *env, jclass cl, jobjectArray argv) {
  char **argv2;
    
  argv2 = initArgs(env, argv, "dump");
  rrd_dump((*env)->GetArrayLength(env, argv)+1, argv2);
  freeArgs(env, argv, argv2);
  if (rrd_test_error())
    throwException(env, "gnu/rrd/RRDException", rrd_get_error());
}

JNIEXPORT void JNICALL Java_gnu_rrd_RRDJNI_restore
(JNIEnv *env, jclass cl, jobjectArray argv) {
  char **argv2;
    
  argv2 = initArgs(env, argv, "restore");
  rrd_restore((*env)->GetArrayLength(env, argv)+1, argv2);
  freeArgs(env, argv, argv2);
  if (rrd_test_error())
    throwException(env, "gnu/rrd/RRDException", rrd_get_error());
}

JNIEXPORT jobject JNICALL Java_gnu_rrd_RRDJNI_last
(JNIEnv *env, jclass cl, jobjectArray argv) {
  char      **argv2;
  time_t    t;
  
  jclass    DateClass;
  jmethodID DateMethodInit;
  jobject   DateObj;
  jlong     timestamp;
  
  argv2 = initArgs(env, argv, "last");
  t = rrd_last((*env)->GetArrayLength(env, argv)+1, argv2);
  timestamp = t;
 
  DateClass      = (*env)->FindClass(env, "java/util/Date");
  DateMethodInit = (*env)->GetMethodID(env, DateClass, "<init>", "(J)V");
  DateObj        = (*env)->NewObject(env, DateClass, DateMethodInit, timestamp*1000);
  
  freeArgs(env, argv, argv2);
  if (rrd_test_error())
    throwException(env, "gnu/rrd/RRDException", rrd_get_error());

  return DateObj;
}

JNIEXPORT jobject JNICALL Java_gnu_rrd_RRDJNI_info
(JNIEnv *env, jclass cl, jobjectArray argv) {
  char      **argv2;
  rrd_info_t    *info, *save;

  jclass        HashtableClass;
  jmethodID     HashtableMethodInit;
  jmethodID     HashtableMethodPut;
  jclass        RRDInfoClass;
  jmethodID     RRDInfoMethodVal;
  jmethodID     RRDInfoMethodCnt;
  jmethodID     RRDInfoMethodStr;
  jmethodID     RRDInfoMethodDate;
  jclass        DateClass;
  jmethodID     DateMethodInit;
  jobject       DateObj;

  jobject       rtnObj;
  jstring       key;
  jdouble       value;
  jlong         counter;
  jstring       strval;
  
  HashtableClass      = (*env)->FindClass(env, "java/util/Hashtable");
  HashtableMethodInit = (*env)->GetMethodID(env, HashtableClass, "<init>", "()V");
  HashtableMethodPut  = (*env)->GetMethodID(env, HashtableClass, "put",
					    "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
  RRDInfoClass        = (*env)->FindClass(env, "gnu/rrd/RRDInfo");
  RRDInfoMethodVal    = (*env)->GetMethodID(env, RRDInfoClass, "<init>", "(D)V");
  RRDInfoMethodCnt    = (*env)->GetMethodID(env, RRDInfoClass, "<init>", "(J)V");
  RRDInfoMethodStr    = (*env)->GetMethodID(env, RRDInfoClass, "<init>", "(Ljava/lang/String;)V");
  RRDInfoMethodDate   = (*env)->GetMethodID(env, RRDInfoClass, "<init>", "(Ljava/util/Date;)V");
  DateClass           = (*env)->FindClass(env, "java/util/Date");
  DateMethodInit      = (*env)->GetMethodID(env, DateClass, "<init>", "(J)V");
  rtnObj              = (*env)->NewObject(env, HashtableClass, HashtableMethodInit, NULL);

  argv2 = initArgs(env, argv, "last");
  info = rrd_info((*env)->GetArrayLength(env, argv)+1, argv2);

  while (info) {
    save=info;
    key = (jstring)(*env)->NewStringUTF(env, info->key);
    switch (info->type) {
      case RD_I_VAL:
        value = info->value.u_val;
        (*env)->CallVoidMethod(env, rtnObj, HashtableMethodPut, key,
          (*env)->NewObject(env, RRDInfoClass, RRDInfoMethodVal, value), NULL);
        break;
      case RD_I_CNT:
	counter = info->value.u_cnt;
	if (strcmp(info->key, "last_update")==0) {
          (*env)->CallVoidMethod(env, rtnObj, HashtableMethodPut, key,
            (*env)->NewObject(env, RRDInfoClass, RRDInfoMethodDate,
	      (*env)->NewObject(env, DateClass, DateMethodInit, counter*1000)), NULL);
	} else {
          (*env)->CallVoidMethod(env, rtnObj, HashtableMethodPut, key,
            (*env)->NewObject(env, RRDInfoClass, RRDInfoMethodCnt, counter), NULL);
	}
        break;
      case RD_I_STR:
        strval = (jstring)(*env)->NewStringUTF(env, info->value.u_str);
        (*env)->CallVoidMethod(env, rtnObj, HashtableMethodPut, key,
          (*env)->NewObject(env, RRDInfoClass, RRDInfoMethodStr, strval), NULL);
        break;
    }
    info = info->next;
    free(save);
  }
  free(info);
  freeArgs(env, argv, argv2);
  if (rrd_test_error())
    throwException(env, "gnu/rrd/RRDException", rrd_get_error());

  return rtnObj;
}

JNIEXPORT jobject JNICALL Java_gnu_rrd_RRDJNI_fetch
(JNIEnv *env, jclass cl, jobjectArray argv) {
  time_t        start, end;
  unsigned long step, ds_cnt, i, ii;
  rrd_value_t   *data, *datai;
  char          **ds_namv;
  char          s[30];
  char          **argv2;
  
  jclass        VectorClass;
  jmethodID     VectorMethodInit;
  jmethodID     VectorMethodAdd;
  jclass        DateClass;
  jmethodID     DateMethodInit;
  jclass        DoubleClass;
  jmethodID     DoubleMethodInit;
  jclass        RRDRecClass;
  jmethodID     RRDRecMethodInit;
  
  jobject       dateObj;
  jdouble       dataObj;
  jobject       dsObj;
  jobject       rtnObj;
  jlong         timestamp;
    
  VectorClass      = (*env)->FindClass(env, "java/util/Vector");
  VectorMethodInit = (*env)->GetMethodID(env, VectorClass, "<init>", "()V");
  VectorMethodAdd  = (*env)->GetMethodID(env, VectorClass, "addElement", "(Ljava/lang/Object;)V");
  DateClass        = (*env)->FindClass(env, "java/util/Date");
  DateMethodInit   = (*env)->GetMethodID(env, DateClass, "<init>", "(J)V");
  DoubleClass      = (*env)->FindClass(env, "java/lang/Double");
  DoubleMethodInit = (*env)->GetMethodID(env, DoubleClass, "<init>", "(D)V");
  RRDRecClass      = (*env)->FindClass(env, "gnu/rrd/RRDRec");
  RRDRecMethodInit = (*env)->GetMethodID(env, RRDRecClass, "<init>", "(Ljava/util/Date;Ljava/util/Vector;)V");
  rtnObj           = (*env)->NewObject(env, VectorClass, VectorMethodInit, NULL);

  argv2 = initArgs(env, argv, "fetch");
  if (rrd_fetch((*env)->GetArrayLength(env, argv)+1, argv2, &start, &end, &step,
      &ds_cnt, &ds_namv, &data) != -1) {
    datai = data;
    for (i = start; i <= end; i += step) {
      timestamp = i;
      dateObj = (*env)->NewObject(env, DateClass, DateMethodInit, timestamp*1000);
      dsObj   = (*env)->NewObject(env, VectorClass, VectorMethodInit, NULL);
      for (ii = 0; ii < ds_cnt; ii++) {
	dataObj = *(datai++);
        (*env)->CallVoidMethod(env, dsObj, VectorMethodAdd,
          (*env)->NewObject(env, DoubleClass, DoubleMethodInit, dataObj));
      }
      (*env)->CallVoidMethod(env, rtnObj, VectorMethodAdd,
        (*env)->NewObject(env, RRDRecClass, RRDRecMethodInit, dateObj, dsObj));
    }
    for (i=0; i<ds_cnt; i++) free(ds_namv[i]);
    free(ds_namv);
    free(data);
  }
  freeArgs(env, argv, argv2);
  if (rrd_test_error())
    throwException(env, "gnu/rrd/RRDException", rrd_get_error());

  return rtnObj;
}

JNIEXPORT void JNICALL Java_gnu_rrd_RRDJNI_tune
(JNIEnv *env, jclass cl, jobjectArray argv) {
  char **argv2;
    
  argv2 = initArgs(env, argv, "tune");
  rrd_tune((*env)->GetArrayLength(env, argv)+1, argv2);
  freeArgs(env, argv, argv2);
  if (rrd_test_error())
    throwException(env, "gnu/rrd/jni/RRDException", rrd_get_error());
}

JNIEXPORT void JNICALL Java_gnu_rrd_RRDJNI_resize
(JNIEnv *env, jclass cl, jobjectArray argv) {
  char **argv2;
    
  argv2 = initArgs(env, argv, "resize");
  rrd_resize((*env)->GetArrayLength(env, argv)+1, argv2);
  freeArgs(env, argv, argv2);
  if (rrd_test_error())
    throwException(env, "gnu/rrd/RRDException", rrd_get_error());
}

static char ** initArgs
(JNIEnv *env, jobjectArray argv, char *cmd) {
  char        **argv2;
  int         i;
  jobject     o;
  const char* element;

  argv2 = calloc((*env)->GetArrayLength(env, argv)+1, sizeof(char *));
  argv2[0] = strdup(cmd);
  for (i = 0; i < (*env)->GetArrayLength(env, argv); i++) {
    o = (*env)->GetObjectArrayElement(env, argv, i);
    element = (*env)->GetStringUTFChars(env, (jstring)o, NULL);
    argv2[i+1] = strdup(element);
  }
  return argv2;
}

static void freeArgs
(JNIEnv *env, jobjectArray argv, char *argv2[]) {
  int i;
  for (i = 0; i < (*env)->GetArrayLength(env, argv); i++) {
    free(argv2[i]);
  }
  free(argv2);
}

static void throwException
(JNIEnv *env, char *exception, char *msg) {
  int rc = (*env)->ThrowNew(env, (*env)->FindClass(env, exception), msg);
  if(rc < 0)  /* couldn't find exception class */
    (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/Exception"), msg);
  rrd_clear_error();
}
