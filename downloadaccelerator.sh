#!/bin/bash

if [ ! -e $SNAP_USER_DATA/.config/autostart/$SNAP_NAME-server.desktop ]
then
  if [ ! -d $SNAP_USER_DATA/.config/autostart ]
  then
    mkdir -p $SNAP_USER_DATA/.config/autostart
  fi
  ln -sfnt $SNAP_USER_DATA/.config/autostart/ $SNAP/autostart/$SNAP_NAME-server.desktop
fi

cd $SNAP

if [ ! -z $1 ]
then
  desktop-launch $JAVA_BIN -jar $SNAP/$SNAPCRAFT_PROJECT_NAME-$SNAPCRAFT_PROJECT_VERSION.jar  client
else
  desktop-launch $JAVA_BIN -jar $SNAP/$SNAPCRAFT_PROJECT_NAME-$SNAPCRAFT_PROJECT_VERSION.jar  server
fi
