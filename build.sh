#!/bin/bash

PROJECT_NAME="downloadaccelerator"
VERSION="3.2"

## create a temporary folder named out
mkdir out && 
## compile the app from src folder
javac -d out -classpath libs/*:.  -sourcepath src/ src/utils/DaServer.java && 
## move jar dependencies and go to out dir
cp libs/*.jar out/ && cd out && 
## extract jar dependencies files
find ./ -name "*.jar" -exec jar -xf {} \; && 
## delete jar files
rm -f *.jar && 
## create the app jar
jar cfe $PROJECT_NAME-$VERSION.jar utils.DaServer . && 
## move jar app to the parent dir and change to the parent dir then delete temporary folder 
cp $PROJECT_NAME-$VERSION.jar ../ && cd .. && rm -rf out
