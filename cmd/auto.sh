#!/bin/bash

if [ $# == 1 ] ; then
	cd ../bin
	java -Djava.class.path=:../lib/log4j-1.2.16.jar main/Auto $1
else
	echo "Usage $0 <server>"
fi
