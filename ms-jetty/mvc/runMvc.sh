#!/bin/bash

../gradlew :mvc:jibBuildTar

docker load --input build/jib-image.tar

docker stop mvc
docker rm mvc


docker run --rm -d --name mvc \
--memory=256m \
--cpus 4 \
--network="host" \
-e JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=80 -XX:MaxRAMPercentage=80" \
localrun/mvc:latest
