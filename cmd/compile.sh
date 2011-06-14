#!/bin/bash

cd ..
rm -rf bin
mkdir bin
mkdir bin/main
mkdir bin/main/java
cd bin/main/java
ln -s ../../../lib lib
ln -s ../../../cfg cfg
ln -s ../../../log log
cd ../../../src/main/java
javac -cp :../../../lib/log4j-1.2.16.jar -d ../../../bin/main/java main/Node.java 
javac -cp :../../../lib/log4j-1.2.16.jar -d ../../../bin/main/java main/Client.java 
javac -cp :../../../lib/log4j-1.2.16.jar -d ../../../bin/main/java main/Server.java 
javac -cp :../../../lib/log4j-1.2.16.jar -d ../../../bin/main/java main/Auto.java 
