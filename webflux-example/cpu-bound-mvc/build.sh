#!/bin/bash

../gradlew :cpu-bound-mvc:build
docker load --input build/jib-image.tar


