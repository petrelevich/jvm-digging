#!/bin/bash

../gradlew clean build

docker load --input build/jib-image.tar

docker run --rm --name spring-boot-demo \
--memory=128m \
--cpus 1 \
-p 8080:8080 \
-p 3000:3000 \
-e JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=80 -XX:MaxRAMPercentage=80 -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=3000 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false" \
localrun/spring-boot-demo:latest
