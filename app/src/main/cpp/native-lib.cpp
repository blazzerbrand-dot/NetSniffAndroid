#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_netsniffandroid_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_netsniffandroid_core_nativebridge_NativeBridge_getNativeVersion(JNIEnv *env,
                                                                                 jobject thiz) {
    // TODO: implement getNativeVersion()
}