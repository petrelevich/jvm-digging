#!/bin/bash

../gradlew :data-source:build

docker load --input build/jib-image.tar

docker stop data-source

docker run --rm -d --name data-source \
--memory=512m \
--cpus 4 \
--network="host" \
-e JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=80 -XX:MaxRAMPercentage=80" \
localrun/data-source:latest
