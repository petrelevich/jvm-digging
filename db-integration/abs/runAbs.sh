#!/bin/bash

../gradlew clean build

docker load --input build/jib-image.tar

docker run --rm --name abs \
--memory=256m \
--cpus 1 \
--network="host" \
-e JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=80 -XX:MaxRAMPercentage=80" \
localrun/abs:latest
