#!/bin/bash

echo "Compiling..."
javac -d out $(find src -name "*.java")

echo "Starting server..."
java -cp out com.mycache.server.CacheServer
