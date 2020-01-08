package com.jiangdg.natives;

/**  YV12、NV21为Camera采集的图片颜色格式；
 *   YUV420p、YUV420sp为编码器(MediaCodec)支持的颜色格式；
 *
 *   注：Camera默认采集的图片为逆时针横屏，角度为0
 *   如果要兼容横屏采集、竖屏采集或者自动校正图片，需要用到角度旋转
 *
 * Created by jiangdongguo on 2019/8/18.
 */

public class YuvUtils {
    public static final int DEGREE_BACK_ROTATE_90 = 90;
    public static final int DEGREE_BACK_ROTATE_180 = 180;
    public static final int DEGREE_BACK_ROTATE_270 = 270;
    public static final int DEGREE_FRONT_ROTATE_270 = 270;
    public static final int DEGREE_FRONT_ROTATE_180 = 180;

    public static native int nativeNV21ToYUV420sp(byte[] data,int width, int height);
    public static native int nativeNV21ToYUV420p(byte[] data,int width, int height);

    public static native int nativeYV12ToNV21(byte[] data,int width, int height);

    // 后置旋转：90、180、270
    public native static int nativeRotateBackNV21(byte[] src,int width, int height,int rotateDegree);

    // 前置旋转：270，180
    public  static native int nativeRotateFrontNV21(byte[] src,int width, int height,int rotateDegree);

    static{
        System.loadLibrary("yuv");
    }
}
