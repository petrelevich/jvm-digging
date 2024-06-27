#!/bin/bash

../gradlew clean build

docker load --input build/jib-image.tar

docker run --rm -d --name request-processor-1 \
-e PRODUCER_NAME="producer-1" \
--network="host" \
localrun/request-processor:latest

docker run --rm -d --name request-processor-2 \
-e PRODUCER_NAME="producer-2" \
--network="host" \
localrun/request-processor:latest

docker run --rm -d --name request-processor-3 \
-e PRODUCER_NAME="producer-3" \
--network="host" \
localrun/request-processor:latest
