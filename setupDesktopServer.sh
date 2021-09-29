#!/bin/bash

if [ ! -e $SNAP_USER_DATA/.config/autostart/$SNAP_NAME-server.desktop ]
then
  if [ ! -d $SNAP_USER_DATA/.config/autostart ]
  then
    mkdir -p $SNAP_USER_DATA/.config/autostart
    ln -sfnt $SNAP_USER_DATA/.config/autostart/ $SNAP/autostart/$SNAP_NAME-server.desktop
  fi
fi
