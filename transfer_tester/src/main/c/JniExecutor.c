#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include "pl_kumon_transfertester_tester_jni_JniExecutor.h"

void readRequest(JNIEnv *env, jbyteArray* requestBytes);
jbyte* prepareResponseRandomBytes(JNIEnv *env, jbyteArray *response, jint responseSize);


JNIEXPORT jbyteArray JNICALL Java_pl_kumon_transfertester_tester_jni_JniExecutor_requestJni(
    JNIEnv *env, jobject thisObj, jbyteArray requestBytes, jint responseSize) {

    readRequest(env, &requestBytes);

    jbyteArray response = (*env)->NewByteArray(env, responseSize);
    jbyte *bytes = prepareResponseRandomBytes(env, &response, responseSize);
    (*env)->SetByteArrayRegion(env, response, 0, responseSize, bytes);
    return response;
}

void readRequest(JNIEnv *env, jbyteArray* requestBytes) {
    unsigned char* buffer = (*env)->GetByteArrayElements(env, *requestBytes, NULL);
    jsize size = (*env)->GetArrayLength(env, *requestBytes);
    (*env)->ReleaseByteArrayElements(env, *requestBytes, buffer, JNI_ABORT);
}

jbyte* prepareResponseRandomBytes(JNIEnv *env, jbyteArray *response, jint responseSize) {
    return (*env)->GetByteArrayElements(env, *response, 0);
}