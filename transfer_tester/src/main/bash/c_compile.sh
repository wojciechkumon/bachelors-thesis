#!/usr/bin/env bash

JAVA_HOME="${1}/.."
C_CODE_PATH=${2:-../c}
JAVA_CODE_PATH=${3:-../java}
OUTPUT_PATH=${4:-././../../../target/classes}

echo "JAVA_HOME: ${JAVA_HOME}"
${JAVA_HOME}/bin/javah -classpath ${JAVA_CODE_PATH} -d ${C_CODE_PATH} -jni pl.kumon.transfertester.tester.jni.JniExecutor

PLATFORM='unknown';
OS=$(uname)
if [[ "$OS" == 'Darwin' ]]; then
    gcc -c -I/System/Library/Frameworks/JavaVM.framework/Headers "${C_CODE_PATH}/JniExecutor.c"
    gcc -dynamiclib -o "${OUTPUT_PATH}/libJniExecutor.dylib" JniExecutor.o -framework JavaVM
    rm JniExecutor.o
else
    gcc -I"${JAVA_HOME}/include" -I"${JAVA_HOME}/include/linux" -o "${OUTPUT_PATH}/libJniExecutor.so" -shared "${C_CODE_PATH}/JniExecutor.c"
fi


rm "${C_CODE_PATH}/pl_kumon_transfertester_tester_jni_JniExecutor.h"
