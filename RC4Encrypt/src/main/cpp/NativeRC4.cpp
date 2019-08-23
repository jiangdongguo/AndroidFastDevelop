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
void create_key_stream(int *keyStream, int textLen);

int S[256]; // 向量S
int T[256]; // 向量T
const char *key_default = "jiangdongguo"; // 默认密钥


extern "C"
JNIEXPORT jint JNICALL
Java_com_jiangdg_natives_RC4Utils_nativeRc4Encrypt(JNIEnv *env, jclass type, jstring key_,
                                                   jbyteArray plainText_, jint start, jint len) {
    const char *key = NULL;
    if(key_ != NULL) {
        key = env->GetStringUTFChars(key_, 0);
    }
    if(plainText_ == NULL) {
        LOG_I("plain text can not be null");
        return -1;
    }
    jbyte *plainText = env->GetByteArrayElements(plainText_, JNI_FALSE);
//    jsize textLen = env->GetArrayLength(plainText_);
    LOG_I("encrypt plainText = %s", plainText);
    // 初始化向量S
    init_S();

    // 初始化密钥，即向量T
    init_key(key);

    // 置换状态向量S
    premute_S();

    // 生成密钥流，长度与明文长度一样
    int * keyStream = (int *) malloc(len * sizeof(int));
    memset(keyStream, 0, len * sizeof(int));
    create_key_stream(keyStream, len);

    // 使用密钥流对明文加密(异或处理)
    char * cryptText = (char *) malloc(len * sizeof(char));
    memset(cryptText, 0, len * sizeof(char));
    for(int i = 0; i< len; i++) {
        cryptText[i] = char(keyStream[i] ^ plainText[i+start]);
    }
    LOG_I("encrypt cryptText = %s /////len=%d", cryptText, strlen(cryptText));
    memcpy(plainText+start, cryptText, len);
    free(cryptText);
    free(keyStream);
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
        key = env->GetStringUTFChars(key_, 0);
    }
    if(cipherText_ == NULL) {
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
    int * keyStream = (int *) malloc(len * sizeof(int));
    memset(keyStream, 0, len * sizeof(int));
    create_key_stream(keyStream, len);

    // 使用密钥流对密文解密(异或处理)
    char * plainText = (char *) malloc(len * sizeof(char));
    memset(plainText, 0, len * sizeof(char));
    for(int i = 0; i< len; i++) {
        plainText[i] = char(keyStream[i] ^ cipherText[i+start]);
    }
    LOG_I("decrypt plainText = %s", plainText);
    memcpy(cipherText+start, plainText, len);
    free(plainText);
    free(keyStream);
    LOG_I("decrypt text over");
    if(key_ != NULL) {
        env->ReleaseStringUTFChars(key_, key);
    }
    env->ReleaseByteArrayElements(cipherText_, cipherText, 0);
    return 0;
}


void init_S() {
    for(int i=0; i<256; i++) {
        S[i] = i;
    }
}

void init_key(const char *key) {
    if(key) {
        LOG_I("使用用户输入密钥");
        int key_len = strlen(key);
        // 将key按字节循环填充到向量T
        for(int i=0; i<256; i++) {
            T[i] = key[i % key_len];
        }
    } else {
        LOG_I("使用默认密钥");
        // 将key_default按字节循环填充到向量T
        int key_len = strlen(key_default);
        for(int i=0; i<256; i++) {
            T[i] = key_default[i % key_len];
        }
    }
}


void premute_S() {
    int temp;
    int j = 0;
    // 打乱向量S，保证每个字节都得到处理
    for (int i = 0; i < 256; i++) {
        j = (j + S[i] + T[i]) % 256;
        temp = S[i];
        S[i] = S[j];
        S[j] = temp;
    }
}


void create_key_stream(int *keyStream, int textLen) {
    int i=0, j=0;
    int index=0, t = 0;
    int temp = 0;
    while (textLen --) {
        i = (i + 1) % 256;
        j = (j + S[i]) % 256;
        // 置换向量S
        temp = S[i];
        S[i] = S[j];
        S[j] = temp;
        // 生成密钥流
        t = (S[i] + S[j]) % 256;
        keyStream[index] = S[t];
        index ++;
    }
}
