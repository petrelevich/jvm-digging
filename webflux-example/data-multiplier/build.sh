#!/bin/bash

../gradlew :data-multiplier:build
docker load --input build/jib-image.tar


