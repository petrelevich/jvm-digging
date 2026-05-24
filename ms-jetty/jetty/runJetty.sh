#!/bin/bash

../gradlew :jetty:jibBuildTar

docker load --input build/jib-image.tar

docker stop jetty
docker rm jetty


docker run --rm -d --name jetty \
--memory=256m \
--cpus 4 \
--network="host" \
-e JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=80 -XX:MaxRAMPercentage=80" \
localrun/jetty:latest
