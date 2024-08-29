#!/bin/bash
hey -n=1000000 -c=30 -m POST -H "Content-Type: application/json" -d "344" http://localhost:8080/request
