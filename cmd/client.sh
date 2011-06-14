#!/bin/bash

if [ $# == 1 ] ; then
	cd ../bin/main/java
	java -Djava.class.path=:./lib/log4j-1.2.16.jar main/Client $1
else
	echo "Usage $0 <server>"
fi
