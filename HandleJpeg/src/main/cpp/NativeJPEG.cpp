// JPEG图形编码压缩、解压
// 采用libjpeg库(libjpeg_turbo版本)实现
//
// Created by Jiangdg on 2019/8/12.
//
#include <jni.h>
#include <android/bitmap.h>
#include <android/log.h>
#include <malloc.h>
#include "jpeglib.h"
#include <stdio.h>
#include <csetjmp>
#include <string.h>
#include <setjmp.h>

#define TAG "NativeJPEG"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)
#define UNSUPPORT_BITMAP_FORMAT -99
#define FAILED_OPEN_OUTPATH -100
#define SUCCESS_COMPRESS 1

typedef uint8_t BYTE;

// 自定义error结构体
struct my_error_mgr {
    struct jpeg_error_mgr pub;
    jmp_buf setjmp_buffer;
};

int compressJPEG(BYTE *data, int width, int height, jint quality, const char *path) {
    int nComponent = 3;
    FILE *f = fopen(path, "wb");
    if(f == NULL) {
        return FAILED_OPEN_OUTPATH;
    }

    // 初始化JPEG对象，为其分配空间
    struct my_error_mgr my_err;
    struct jpeg_compress_struct jcs;
    jcs.err = jpeg_std_error(&my_err.pub);
    if(setjmp(my_err.setjmp_buffer)) {
        return 0;
    }
    jpeg_create_compress(&jcs);

    // 指定压缩数据源，设定压缩参数
    // 使用哈夫曼算法压缩编码
    jpeg_stdio_dest(&jcs, f);
    jcs.image_width = width;
    jcs.image_height = height;
    jcs.arith_code = false; // false->哈夫曼编码
    jcs.input_components = nComponent;
    jcs.in_color_space = JCS_RGB;
    jpeg_set_defaults(&jcs);
    jcs.optimize_coding = quality;  // 压缩质量 0~100
    jpeg_set_quality(&jcs, quality, true);
    // 开始压缩，一行一行处理
    jpeg_start_compress(&jcs, true);
    JSAMPROW row_point[1];
    int row_stride;
    row_stride = jcs.image_width * nComponent;
    while(jcs.next_scanline < jcs.image_height) {
        row_point[0] = &data[jcs.next_scanline * row_stride];
        jpeg_write_scanlines(&jcs, row_point, 1);
    }
    // 结束压缩，释放资源
    if(jcs.optimize_coding != 0) {
        LOGI("使用哈夫曼压缩编码完成");
    }
    jpeg_finish_compress(&jcs);
    jpeg_destroy_compress(&jcs);
    fclose(f);
    return SUCCESS_COMPRESS;
}

const char *jstringToString(JNIEnv *env, jstring jstr) {
    char *ret;
    const char * c_str = env->GetStringUTFChars(jstr, NULL);
    jsize len = env->GetStringLength(jstr);
    if(c_str != NULL) {
        ret = (char *)malloc(len+1);
        memcpy(ret, c_str, len);
        ret[len] = 0;
    }
    env->ReleaseStringUTFChars(jstr, c_str);
    return ret;
}

extern  "C"
JNIEXPORT jint JNICALL
Java_com_jiangdg_natives_JPEGUtils_nativeCompressJPEG(JNIEnv *env, jclass type, jobject bitmap,
                                                      jint quality, jstring outPath_) {
    // 获取bitmap的属性信息
    int ret;
    int width, height, format;
    int color;
    BYTE r, g, b;
    BYTE *pixelsColor;
    BYTE *data, *tmpData;
    AndroidBitmapInfo androidBitmapInfo;
    const char *outPath = jstringToString(env, outPath_);
    LOGI("outPath=%s", outPath);
    if((ret = AndroidBitmap_getInfo(env, bitmap, &androidBitmapInfo)) < 0) {
        LOGI("AndroidBitmap_getInfo failed, error=%d", ret);
        return ret;
    }
    if((ret = AndroidBitmap_lockPixels(env, bitmap, reinterpret_cast<void **>(&pixelsColor))) < 0) {
        LOGI("AndroidBitmap_lockPixels failed, error=%d", ret);
        return ret;
    }
    width = androidBitmapInfo.width;
    height = androidBitmapInfo.height;
    format = androidBitmapInfo.format;
    LOGI("open image:w=%d, h=%d, format=%d", width, height, format);
    // 将bitmap转换为rgb数据,只处理RGBA_8888格式
    // 一行一行的处理，每个像素占4个字节，包括a、r、g、b三个分量，每个分量占8位
    data = (BYTE *)malloc(width * height * 3);
    tmpData = data;
    for(int i=0; i<height; ++i) {
        for(int j=0; j<width; ++j) {
            if(format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
                color = *((int *)pixelsColor);
                b = (color >> 16) & 0xFF;
                g = (color >> 8) & 0xFF;
                r = (color >> 0) & 0xFF;
                *data = r;
                *(data + 1) = g;
                *(data + 2) = b;
                data += 3;
                // 处理下一个像素，在内存中即占4个字节
                pixelsColor += 4;
            } else {
                return UNSUPPORT_BITMAP_FORMAT;
            }
        }
    }
    if((ret = AndroidBitmap_unlockPixels(env, bitmap)) < 0) {
        LOGI("AndroidBitmap_unlockPixels failed,error=%d", ret);
        return ret;
    }
    // 编码压缩图片
    ret = compressJPEG(tmpData, width, height, quality, outPath);
    free((void *)tmpData);
    return ret;
}

