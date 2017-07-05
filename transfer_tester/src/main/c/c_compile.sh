#!/usr/bin/env bash

cc -c -I/System/Library/Frameworks/JavaVM.framework/Headers JniExecutor.c
cc -dynamiclib -o ././../../../target/classes/JniExecutor.jnilib JniExecutor.o -framework JavaVM

rm JniExecutor.o