#!/bin/bash

../gradlew :cpu-bound:build
docker load --input build/jib-image.tar


