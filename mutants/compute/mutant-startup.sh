#!/bin/sh
# Parametros
OWNER=wapl
TAGS=mutant
VERSION=0.0.1-SNAPSHOT.jar
BUCKET=${TAGS}${OWNER}
APPJAR=${TAGS}-${VERSION}

# Get the files we need
gsutil cp gs://${BUCKET}/${APPJAR} .

# Install dependencies
apt-get update
apt-get -y --force-yes install openjdk-8-jdk

# Make Java 8 default
update-alternatives --set java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java

# Start server
java -jar ${APPJAR} .