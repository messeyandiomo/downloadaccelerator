#!/bin/bash

if [ ! -d "$SNAP_USER_DATA/.config/autostart" ];
  then
    mkdir -p $SNAP_USER_DATA/.config/autostart
    ln -sfnt $SNAP_USER_DATA/.config/autostart/ $SNAP/autostart/$SNAP_NAME-server.desktop
  fi
