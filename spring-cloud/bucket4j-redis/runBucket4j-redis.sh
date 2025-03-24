#!/bin/bash

../gradlew :bucket4j-redis:build

docker load --input build/jib-image.tar

docker stop bucket4j-redis-1
docker stop bucket4j-redis-2

docker run --rm -d --name bucket4j-redis-1 \
--memory=256m \
--cpus 1 \
--network="host" \
-e JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=80 -XX:MaxRAMPercentage=80" \
-e SERVER_PORT=8081 \
-e APPLICATION_REDIS_CLIENT-NAME="client-1" \
localrun/bucket4j-redis:latest

docker run --rm -d --name bucket4j-redis-2 \
--memory=256m \
--cpus 1 \
--network="host" \
-e JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=80 -XX:MaxRAMPercentage=80" \
-e SERVER_PORT=8082 \
-e APPLICATION_REDIS_CLIENT-NAME="client-2" \
localrun/bucket4j-redis:latest

