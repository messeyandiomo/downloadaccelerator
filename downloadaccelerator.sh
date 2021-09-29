#!/bin/bash

export JAVA_HOME=$SNAP/usr/lib/jvm/java-11-openjdk-$SNAP_ARCH
export PATH=$SNAP/bin:$SNAP/sbin:$SNAP/usr/bin:$SNAP/usr/sbin:$JAVA_HOME/bin:$PATH
export LD_LIBRARY_PATH=$SNAP/lib:$SNAP/lib/$SNAP_ARCH_TRIPLET:$SNAP/usr/lib:$SNAP/usr/lib/$SNAP_ARCH_TRIPLET:$SNAP_LIBRARY_PATH:$LD_LIBRARY_PATH

DACAUTOSTART=$SNAP_USER_DATA/.config/autostart
DACAUTOSTARTDESKTOP=$SNAP_NAME-server.desktop
DACJARPATHNAME=$SNAP/$SNAP_NAME-$SNAP_VERSION.jar


###creation of autostart desktop file
if [ ! -e $DACAUTOSTART ]
then
	mkdir -p $DACAUTOSTART
fi

cd $SNAP

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
