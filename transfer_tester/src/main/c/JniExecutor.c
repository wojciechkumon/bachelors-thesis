#include <jni.h>
#include <stdio.h>
#include "pl_kumon_transfertester_tester_jni_JniExecutor.h"

JNIEXPORT jstring JNICALL Java_pl_kumon_transfertester_tester_jni_JniExecutor_stringFromJni(JNIEnv *env, jobject thisObj) {
    printf("Hello JNI World!\n");
    return NULL;
}