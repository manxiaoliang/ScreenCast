#include <iostream>
#include <jni.h>
#include <string.h>
#include "cast_controller.h"
#include "tcp_stream_socket_client.h"

using namespace vcast::client;
std::unique_ptr<CastController> mCastController = NULL; 
static bool mConnected  = false;



extern "C" {
JNIEXPORT jstring JNICALL Java_com_intel_screencastfrontclient_CastJNILib_stringFromJNI(JNIEnv* env,jobject obj);
JNIEXPORT void JNICALL Java_com_intel_screencastfrontclient_CastJNILib_startCast(JNIEnv* env,jobject obj);
JNIEXPORT void JNICALL Java_com_intel_screencastfrontclient_CastJNILib_stopCast(JNIEnv* env,jobject obj);
JNIEXPORT void JNICALL Java_com_intel_screencastfrontclient_CastJNILib_init(JNIEnv* env,jobject obj,jstring ip,jint port);
};

JNIEXPORT jstring JNICALL Java_com_intel_screencastfrontclient_CastJNILib_stringFromJNI(JNIEnv* env,jobject /* this */) {
    std::string hello = "Wecome to use Screen Cast !";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT void JNICALL Java_com_intel_screencastfrontclient_CastJNILib_init(JNIEnv* env,jobject obj,jstring ip,jint port) {
    system("'/system/bin/hw/probe-node'");

    vcast::client::TcpConfigInfo cfg;
    const char* str;
    jboolean iscopy = false;
    str = env->GetStringUTFChars(ip, &iscopy);
    if(str == NULL) {
        LOGE("The Ip address is wrong");
    }
    std::string ipaddr(str);
    cfg.tcp_conn_info.ip_addr = ipaddr;
    cfg.tcp_conn_info.port = port;
    LOGE("The Ip address is ip = %s,port = %d",ipaddr.c_str(),port);
    mCastController = std::make_unique<vcast::client::CastController>(cfg);
    if (mCastController)
    {
       auto[connected, error_msg] =  mCastController->start();
       mConnected = connected;
       LOGI("The connect status: %d,error message: %s",(int)connected,error_msg.c_str());
    }
}

JNIEXPORT void JNICALL Java_com_intel_screencastfrontclient_CastJNILib_startCast(JNIEnv* env,jobject obj) {
    LOGI("The connect status:1");
    if(mCastController && mConnected)
       mCastController->startBackendApp();
}

JNIEXPORT void JNICALL Java_com_intel_screencastfrontclient_CastJNILib_stopCast(JNIEnv* env,jobject obj) {
    if(mCastController && mConnected)
       mCastController->stopBackendApp();
}

