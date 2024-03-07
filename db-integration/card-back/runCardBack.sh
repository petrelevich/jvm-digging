#!/bin/bash

../gradlew clean build

docker load --input build/jib-image.tar

docker run --rm -d --name card-back-1 \
--memory=256m \
--cpus 1 \
--network="host" \
-e JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=80 -XX:MaxRAMPercentage=80" \
localrun/card-back:latest

docker run --rm -d --name card-back-2 \
--memory=256m \
--cpus 1 \
--network="host" \
-e JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=80 -XX:MaxRAMPercentage=80" \
localrun/card-back:latest

