#!/bin/bash

export JAVA_HOME=$SNAP/usr/lib/jvm/java-11-openjdk-$SNAP_ARCH
export PATH=/snap/bin:$JAVA_HOME/bin:$PATH

DACAUTOSTART=$SNAP_USER_DATA/.config/autostart
DACAUTOSTARTDESKTOP=$SNAP_NAME-server.desktop
DACJARPATHNAME=$SNAP/$SNAP_NAME-$SNAP_VERSION.jar


cd $SNAP

###creation of autostart desktop file
if [ ! -e $DACAUTOSTART ]
then
	mkdir -p $DACAUTOSTART
fi
if [ ! -e $DACAUTOSTART/$DACAUTOSTARTDESKTOP ]
then
	cp $DACAUTOSTARTDESKTOP $DACAUTOSTART
	setsid java -jar $DACJARPATHNAME server &
fi

###execution of the command
if [ -z $1 ]
then
	java -jar $DACJARPATHNAME client
else
	java -jar $DACJARPATHNAME server
fi
