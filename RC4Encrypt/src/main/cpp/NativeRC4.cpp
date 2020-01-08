// RC4加解密native实现
//
// Created by Jiangdg on 2019/8/20.
//
#include <jni.h>
#include <android/log.h>
#include <cstring>

#define TAG "NativeRC4"
#define LOG_I(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

void init_S();
void init_key(const char *key);
void premute_S();
void create_key_stream(char *keyStream, int lenBytes);
const char *jstringToString(JNIEnv *pEnv, jstring pJstring);

char S[256]; // 向量S
char T[256]; // 向量T
bool isDebug = false;
const char *key_default = "teligen@jiangdg"; // 默认密钥


extern "C"
JNIEXPORT jint JNICALL
Java_com_jiangdg_natives_RC4Utils_nativeRc4Encrypt(JNIEnv *env, jclass type, jstring key_,
                                                   jbyteArray plainText_, jint start, jint len) {
    const char *key = NULL;
    if(key_ != NULL) {
        key = jstringToString(env, key_);
    }
    if(plainText_ == NULL) {
        if(isDebug)
            LOG_I("plain text can not be null");
        return -1;
    }
    jbyte *plainText = env->GetByteArrayElements(plainText_, JNI_FALSE);
//    jsize textLen = env->GetArrayLength(plainText_);
    if(isDebug)
        LOG_I("encrypt plainText = %s", plainText);
    // 初始化向量S
    init_S();

    // 初始化密钥，即向量T
    init_key(key);

    // 置换状态向量S
    premute_S();

    // 生成密钥流，长度与明文长度一样
    char * keyStream = (char *) malloc(len * sizeof(char));
    memset(keyStream, 0, len * sizeof(char));
    create_key_stream(keyStream, len);

    // 使用密钥流对明文加密(异或处理)
    char * cryptText = (char *) malloc(len * sizeof(char));
    memset(cryptText, 0, len * sizeof(char));
    for(int i = 0; i< len; i++) {
        cryptText[i] = keyStream[i] ^ plainText[i+start];
    }
    if(isDebug)
        LOG_I("encrypt cryptText = %s /////len=%d", cryptText, strlen(cryptText));
    memcpy(plainText+start, cryptText, len);
    free(cryptText);
    free(keyStream);
    if(isDebug)
        LOG_I("encrypt text over");
    if(key_ != NULL) {
        env->ReleaseStringUTFChars(key_, key);
    }
    env->ReleaseByteArrayElements(plainText_, plainText, 0);
    return 0;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_jiangdg_natives_RC4Utils_nativeRC4Decrypt(JNIEnv *env, jclass type, jstring key_,
                                                   jbyteArray cipherText_, jint start, jint len) {
    const char *key = NULL;
    if(key_ != NULL) {
        key = jstringToString(env,key_);
    }
    if(cipherText_ == NULL) {
        if(isDebug)
            LOG_I("cipher text can not be null");
        return -1;
    }
    jbyte *cipherText = env->GetByteArrayElements(cipherText_, JNI_FALSE);
//    jsize textLen = env->GetArrayLength(cipherText_);
//    LOG_I("decrypt cipherText = %s /////len=%d", cipherText, textLen);
    // 初始化向量S
    init_S();

    // 初始化密钥，即向量T
    init_key(key);

    // 置换状态向量S
    premute_S();

    // 生成密钥流，长度与密文长度一样
    char * keyStream = (char *) malloc(len * sizeof(char));
    memset(keyStream, 0, len * sizeof(char));
    create_key_stream(keyStream, len);

    // 使用密钥流对密文解密(异或处理)
    char * plainText = (char *) malloc(len * sizeof(char));
    memset(plainText, 0, len * sizeof(char));
    for(int i = 0; i< len; i++) {
        plainText[i] = keyStream[i] ^ cipherText[i+start];
    }
    if(isDebug)
        LOG_I("decrypt plainText = %s", plainText);
    memcpy(cipherText+start, plainText, len);
    free(plainText);
    free(keyStream);
    if(isDebug)
        LOG_I("decrypt text over");
    if(key_ != NULL) {
        env->ReleaseStringUTFChars(key_, key);
    }
    env->ReleaseByteArrayElements(cipherText_, cipherText, 0);
    return 0;
}

const char *jstringToString(JNIEnv *env, jstring j_str) {
    const char * c_str = env->GetStringUTFChars(j_str, JNI_FALSE);
    int len = env->GetStringLength(j_str);
    char * ret;
    if(c_str) {
        ret = (char *)malloc(len + 1);
        memcpy(ret, c_str, len);
        ret[len] = 0;
    }
    env->ReleaseStringUTFChars(j_str, c_str);
    return ret;
}


void init_S() {
    for(int i=0; i<256; i++) {
        S[i] = i;
    }
}

void init_key(const char *key) {
    if(key) {
        if(isDebug)
            LOG_I("使用用户输入密钥");
        int key_len = strlen(key);
        // 将key按字节循环填充到向量T
        for(int i=0; i<256; i++) {
            T[i] = key[i % key_len];
        }
    } else {
        if(isDebug)
            LOG_I("使用默认密钥");
        // 将key_default按字节循环填充到向量T
        int key_len = strlen(key_default);
        for(int i=0; i<256; i++) {
            T[i] = key_default[i % key_len];
        }
    }
}


void premute_S() {
    int j = 0;
    // 打乱向量S，保证每个字节都得到处理
    for (int i = 0; i < 256; i++) {
        j = (j + S[i] + T[i]) % 256;
//        temp = S[i];
//        S[i] = S[j];
//        S[j] = temp;
        int temp = 0;
        temp = S[j];
        S[j] = S[i];
        S[i] = temp;
    }
}


void create_key_stream(char *keyStream, int textLen) {
    int i=0, j=0;
    int index=0, t = 0;
//    char temp = 0;
    while (textLen --) {
        i = (i + 1) % 256;
        j = (j + S[i]) % 256;
        // 置换向量S
//        temp = S[i];
//        S[i] = S[j];
//        S[j] = temp;
        int tmp = 0;
        tmp = S[j];
        S[j] = S[i];
        S[i] = tmp;
        // 生成密钥流
        t = (S[i] + S[j]) % 256;
        keyStream[index] = S[t];
        index ++;
    }
}
