#!/bin/bash

../gradlew clean build

docker load --input build/jib-image.tar

docker stop rest-service-synch

docker run --rm --name rest-service-synch \
--memory=256m \
--cpus 2 \
-p 8080:8080 \
-p 3002:3000 \
-e JAVA_TOOL_OPTIONS="-XX:InitialRAMPercentage=80 -XX:MaxRAMPercentage=80 -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=3000 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false" \
localrun/rest-service-synch
