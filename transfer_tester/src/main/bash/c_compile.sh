#!/usr/bin/env bash

C_CODE_PATH=${1:-../c}
JAVA_CODE_PATH=${2:-../java}
OUTPUT_PATH=${3:-././../../../target/classes}


javah -classpath ${JAVA_CODE_PATH} -d ${C_CODE_PATH} -jni pl.kumon.transfertester.tester.jni.JniExecutor

cc -c -I/System/Library/Frameworks/JavaVM.framework/Headers "${C_CODE_PATH}/JniExecutor.c"
cc -dynamiclib -o "${OUTPUT_PATH}/libJniExecutor.dylib" JniExecutor.o -framework JavaVM

rm JniExecutor.o
rm "${C_CODE_PATH}/pl_kumon_transfertester_tester_jni_JniExecutor.h"
