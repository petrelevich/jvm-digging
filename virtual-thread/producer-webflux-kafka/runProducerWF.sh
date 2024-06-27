#!/bin/bash

../gradlew :producer-webflux-kafka:clean
../gradlew :producer-webflux-kafka:spotlessApply
../gradlew :producer-webflux-kafka:build

docker load --input build/jib-image.tar

docker run --rm --name producer-webflux \
--memory=256m \
--cpus 1 \
--network="host" \
-e JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=80 -XX:MaxRAMPercentage=80" \
localrun/producer-webflux-kafka:latest

