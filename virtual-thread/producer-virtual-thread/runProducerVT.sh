#!/bin/bash

../gradlew :producer-virtual-thread:clean
../gradlew :producer-virtual-thread:spotlessApply
../gradlew :producer-virtual-thread:build

docker load --input build/jib-image.tar

docker run --rm --name producer-virtual \
--memory=256m \
--cpus 1 \
--network="host" \
-e JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=80 -XX:MaxRAMPercentage=80" \
localrun/producer-virtual-thread:latest

