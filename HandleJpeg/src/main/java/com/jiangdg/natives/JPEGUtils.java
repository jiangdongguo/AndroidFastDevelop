package com.jiangdg.natives;

import android.graphics.Bitmap;

/**
 *  使用libjpeg实现JPEG编码压缩、解压
 *
 * @author Jiangdg
 * @since 2019-08-12 09:54:00
 * */
public class JPEGUtils {
    static {
        System.loadLibrary("jpegutil");
    }

    public native static int nativeCompressJPEG(Bitmap bitmap, int quality, String outPath);
}
