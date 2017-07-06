#!/usr/bin/env bash

# javah -jni pl.kumon.transfertester.tester.jni.JniExcecutor

cc -c -I/System/Library/Frameworks/JavaVM.framework/Headers JniExecutor.c
cc -dynamiclib -o ././../../../target/classes/JniExecutor.jnilib JniExecutor.o -framework JavaVM

rm JniExecutor.o