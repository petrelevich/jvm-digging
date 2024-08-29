#!/bin/bash

java -Xms256m -Xmx256m -Djdk.tracePinnedThreads=full -jar ./build/libs/producer-virtual-thread-0.0.0-105.f6c04dd0.dirty-SNAPSHOT.jar

