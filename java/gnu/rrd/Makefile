prefix   = /usr/local/rrdtool-1.0.33
top_dir  = ../../..
java_dir = ../..

RRD_SRC_DIR   = $(top_dir)/src
GD_LIB_DIR    = $(top_dir)/gd1.3
JAVA_HOME     = /usr/java
RRD_LIBS      = -L$(top_dir)/src/.libs -lrrd
JAVA_INCLUDE  = $(JAVA_HOME)/include
JAVA_PLATFORM = solaris
JAVA_BIN      = /usr/bin

classes=RRDRec.class \
	RRDGraph.class \
	RRDInfo.class \
	RRDException.class \
	RRDJNI.class \
	RRDTool.class

ALL: classes headers librrdjni.so

classes: $(classes)

headers: 
	test -f RRDJNI.h || rm -rf RRDJNI.h
	$(JAVA_BIN)/javah -classpath $(java_dir)/ -jni gnu.rrd.RRDJNI
	mv gnu_rrd_RRDJNI.h RRDJNI.h

jni:	librrdjni.so

install: 
	cp librrdjni.so $(prefix)/lib

librrdjni.so: RRDJNI.c RRDJNI.h
	$(CC) -G -I$(RRD_SRC_DIR) -I$(GD_LIB_DIR) -I$(JAVA_INCLUDE) -I$(JAVA_INCLUDE)/solaris RRDJNI.c -o $@ $(RRD_LIBS) 

../%.class:
	$(MAKE) -C $(@D) $(@F)

%.class : %.java
	$(JAVA_BIN)/javac -classpath $(java_dir)/ $<

clean:  
	@rm -rf *.class *.so *~

