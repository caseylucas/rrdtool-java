# rrdtool-java
basic java (JNI) calls into rrdtool

This code imported from the original work by David A. Neitz for rrdtool/java integration. See http://oss.oetiker.ch/rrdtool/pub/contrib/.

In order to build, you will need the librrd-dev package. i.e.
```
  sudo apt-get install librrd-dev
```
Make sure that JAVA_HOME environment variable is set, then in the java directory run make:
```
cd java
make
```
