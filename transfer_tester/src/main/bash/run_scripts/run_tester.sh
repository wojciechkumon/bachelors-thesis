#!/usr/bin/env bash

java -jar transfer-tester.jar testType=JNI requestSize=8192 responseSize=8192 numberOfTests=1000 warmUpTests=100 csvFilePath=csv/jni_8192_8192.csv
