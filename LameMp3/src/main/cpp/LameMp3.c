//
// Created by jianddongguo on 2017/6/14.
//
#include <jni.h>
#include "LameMp3.h"
#include "lame/lame.h"

static lame_global_flags *gfp = NULL;

JNIEXPORT void JNICALL
Java_com_jiangdg_natives_LameMp3_lameInit(JNIEnv *env, jclass type, jint inSampleRate,
jint outChannelNum, jint outSampleRate, jint outBitRate,
        jint quality) {
    if(gfp != NULL){
        lame_close(gfp);
        gfp = NULL;
    }
    //  初始化
    gfp = lame_init();
    LOGI("初始化lame库完成");
    //配置参数
    lame_set_in_samplerate(gfp,inSampleRate);
    lame_set_num_channels(gfp,outChannelNum);
    lame_set_out_samplerate(gfp,outSampleRate);
    lame_set_brate(gfp,outBitRate);
    lame_set_quality(gfp,quality);
    lame_init_params(gfp);
    LOGI("配置lame参数完成");
}

JNIEXPORT jint JNICALL
        Java_com_jiangdg_natives_LameMp3_lameFlush(JNIEnv *env, jclass type, jbyteArray mp3buf_) {
    jbyte *mp3buf = (*env)->GetByteArrayElements(env, mp3buf_, NULL);
    jsize len = (*env)->GetArrayLength(env,mp3buf_);
    int resut = lame_encode_flush(gfp,mp3buf,len);
    (*env)->ReleaseByteArrayElements(env, mp3buf_, mp3buf, 0);
    LOG_I("写入mp3数据到文件，返回结果=%d",resut);
    return  resut;
}

JNIEXPORT void JNICALL
Java_com_jiangdg_natives_LameMp3_lameClose(JNIEnv *env, jclass type) {
    lame_close(gfp);
    gfp = NULL;
    LOGI("释放lame资源");
}

JNIEXPORT jint JNICALL
Java_com_jiangdg_natives_LameMp3_lameEncode(JNIEnv *env, jclass type, jshortArray letftBuf_,
                                              jshortArray rightBuf_, jint sampleRate,
                                              jbyteArray mp3Buf_) {
    if(letftBuf_ == NULL || mp3Buf_ == NULL){
        LOGI("letftBuf和rightBuf 或mp3Buf_不能为空");
        return -1;
    }
    jshort *letftBuf = NULL;
    jshort *rightBuf = NULL;
    if(letftBuf_ != NULL){
        letftBuf = (*env)->GetShortArrayElements(env, letftBuf_, NULL);
    }
    if(rightBuf_ != NULL){
        rightBuf = (*env)->GetShortArrayElements(env, rightBuf_, NULL);
    }
    jbyte *mp3Buf = (*env)->GetByteArrayElements(env, mp3Buf_, NULL);
    jsize readSizes = (*env)->GetArrayLength(env,mp3Buf_);
    // 编码
    int result = lame_encode_buffer(gfp,letftBuf,rightBuf,sampleRate,mp3Buf,readSizes);

    // 释放资源
    if(letftBuf_ != NULL){
        (*env)->ReleaseShortArrayElements(env, letftBuf_, letftBuf, 0);
    }
    if(rightBuf_ != NULL){
        (*env)->ReleaseShortArrayElements(env, rightBuf_, rightBuf, 0);
    }
    (*env)->ReleaseByteArrayElements(env, mp3Buf_, mp3Buf, 0);
    LOG_I("编码pcm为mp3，数据长度=%d",result);
    return  result;
}