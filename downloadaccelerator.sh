#!/bin/bash

export JAVA_HOME=$SNAP/usr/lib/jvm/java-11-openjdk-amd64
export PATH=/snap/bin:$JAVA_HOME/bin:$PATH

DACAUTOSTART=$SNAP_USER_DATA/.config/autostart
DACAUTOSTARTDESKTOP=downloadaccelerator-server.desktop


cd $SNAP

###creation of autostart desktop file
if [ ! -e $DACAUTOSTART ]
then
	mkdir -p $DACAUTOSTART
fi
if [ ! -e $DACAUTOSTART/$DACAUTOSTARTDESKTOP ]
then
	cp $DACAUTOSTARTDESKTOP $DACAUTOSTART
	setsid java -jar $SNAP/downloadaccelerator-2.0.jar server &
fi

###execution of the command
if [ -z $1 ]
then
	java -jar $SNAP/downloadaccelerator-2.0.jar client
else
	java -jar $SNAP/downloadaccelerator-2.0.jar server
fi
