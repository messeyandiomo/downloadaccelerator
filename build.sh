#!/bin/bash

PROJECT_NAME="downloadaccelerator"
VERSION="3.2"

mkdir out && javac -d out -classpath libs/*:.  -sourcepath src/ src/utils/DaServer.java && cp libs/*.jar out/ && cp MANIFEST.MF out/ && cd out && jar cfm $PROJECT_NAME-$VERSION.jar  MANIFEST.MF . && cp $PROJECT_NAME-$VERSION.jar ../ && cd .. && rm -rf out
