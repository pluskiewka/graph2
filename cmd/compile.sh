#!/bin/bash

cd ..
rm -rf bin
mkdir bin
cd bin
ln -s ../lib lib
ln -s ../cfg cfg
ln -s ../log log
cd ../src
javac -cp :../lib/log4j-1.2.16.jar -d ../bin/ main/Node.java 
javac -cp :../lib/log4j-1.2.16.jar -d ../bin/ main/Client.java 
javac -cp :../lib/log4j-1.2.16.jar -d ../bin/ main/Server.java 
javac -cp :../lib/log4j-1.2.16.jar -d ../bin/ main/Auto.java 
