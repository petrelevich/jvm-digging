#!/bin/bash

../gradlew :stress-test:build
docker load --input build/jib-image.tar


