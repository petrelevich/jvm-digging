#!/bin/bash

../gradlew clean build

docker load --input build/jib-image.tar

docker stop docker

docker run --rm --name calculations-vthread \
--memory=256m \
--cpus 2 \
-p 3002:3000 \
-e JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=80 -XX:MaxRAMPercentage=80 -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=3000 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false" \
localrun/calculations-vthread:latest
