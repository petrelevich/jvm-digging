#!/bin/bash

../gradlew clean build

docker load --input build/jib-image.tar

docker stop counter-1
docker stop counter-2

docker run --rm -d --name counter-1 \
--memory=256m \
--cpus 1 \
--network="host" \
-e JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=80 -XX:MaxRAMPercentage=80" \
localrun/counter:latest

docker run --rm -d --name counter-2 \
--memory=256m \
--cpus 1 \
--network="host" \
-e JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=80 -XX:MaxRAMPercentage=80" \
-e SERVER_PORT=8081 \
-e APPLICATION_INIT-VALUE=200 \
-e APPLICATION_CLIENT-NAME="counter-2" \
localrun/counter:latest

