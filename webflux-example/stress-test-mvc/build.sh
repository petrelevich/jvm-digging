#!/bin/bash

../gradlew :stress-test-mvc:build
docker load --input build/jib-image.tar


