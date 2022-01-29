#!/bin/bash

if [ ! -e $SNAP_USER_DATA/.config/autostart/$SNAP_NAME-server.desktop ]
then
  if [ ! -d $SNAP_USER_DATA/.config/autostart ]
  then
    mkdir -p $SNAP_USER_DATA/.config/autostart
  fi
  ln -sfnt $SNAP_USER_DATA/.config/autostart/ $SNAP/autostart/$SNAP_NAME-server.desktop
  export PATH=$PATH:$SNAP/bin:$SNAP/sbin:$SNAP/usr/bin:$SNAP/usr/sbin:$JAVA_HOME/bin:
  export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$SNAP_LIBRARY_PATH:$SNAP/lib:$SNAP/lib/$SNAPCRAFT_ARCH_TRIPLET:$SNAP/usr/lib:$SNAP/usr/lib/$SNAPCRAFT_ARCH_TRIPLET
  $JAVA_BIN -jar $SNAP/$SNAP_NAME-$SNAP_VERSION.jar  server &
fi

cd $SNAP

if [ -z $1 ]
then
  $JAVA_BIN -jar $SNAP/$SNAP_NAME-$SNAP_VERSION.jar  client
elif [ $1 = "server" ]
then
  $JAVA_BIN -jar $SNAP/$SNAP_NAME-$SNAP_VERSION.jar  server
fi
