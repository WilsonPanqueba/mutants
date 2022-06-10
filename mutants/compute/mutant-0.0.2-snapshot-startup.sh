#!/bin/sh
# Parametros
OWNER=wapl
TAGS=mutant
VERSION=0.0.2-SNAPSHOT.jar
BUCKET=mutantwapl
APPJAR=mutant-0.0.2-SNAPSHOT.jar
DBUSER=wapl
DBPASS=secret_wapl
SQL=dml.sql
PERMISOS=permisos.sql

# Get the files we need
gsutil cp gs://mutantwapl/mutant-0.0.2-SNAPSHOT.jar .
gsutil cp gs://mutantwapl/dml.sql .
gsutil cp gs://mutantwapl/permisos.sql .


# Install dependencies
apt-get update

# Install database
apt-get -y --force-yes install postgresql
sudo -u postgres psql -c "\i dml.sql"
sudo -u postgres psql -c "\i permisos.sql"
systemctl restart postgresql.service

export POSTGRESQL_USER=wapl
export POSTGRESQL_PASSWORD=secret_wapl

# Make Java 8 defaultR:
apt-get  -y  install openjdk-11-jdk

# Start server
java -jar mutant-0.0.2-SNAPSHOT.jar .