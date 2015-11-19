prefix = /usr/local/rrdtool-1.0.33

JAVA_BIN=$(JAVA_HOME)/bin

ALL: subdirs jar

subdirs: 
	$(MAKE) -C gnu ALL

classes: 
	$(MAKE) -C gnu classes

headers: 
	$(MAKE) -C gnu headers

jni:
	$(MAKE) -C gnu jni

jar:
	$(JAVA_BIN)/jar cf rrdjni.jar gnu/rrd/*.class

docs:
	test -d ../doc/java || mkdir ../doc/java;
	$(JAVA_BIN)/javadoc -J-ms32m -J-mx32m -d ../doc/java -version -private gnu.rrd 

install:
	cp rrdjni.jar $(prefix)/lib
	$(MAKE) -C gnu install

clean:
	test -d ../doc/java || rm -rf ../doc/java
	rm -rf rrdjni.jar
	$(MAKE) -C gnu clean
