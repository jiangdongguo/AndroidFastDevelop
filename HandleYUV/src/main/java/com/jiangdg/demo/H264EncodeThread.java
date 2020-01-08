package com.jiangdg.demo;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Build;

import com.jiangdg.natives.YuvUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *  编码线程
 *
 * @author Jiangdg
 * @since 2019-08-22 11:30
 * */
public class H264EncodeThread extends Thread{
    private static final String MIME = "video/avc";
    private static final long TIMESOUTUS = 10000;
    private MediaCodec mEncodeCodec;
    private int mWidth;
    private int mHeight;
    private boolean isExit = false;
    // 队列
    private ArrayBlockingQueue mQueue;
    private OnEncodeResultListener mListener;
    private boolean isCodecStart;
    // 横竖屏拍摄切换
    private boolean isVerticalShoot = true;
    private int mColorFormat;

    // 数据回调监听器
    public interface OnEncodeResultListener{
        void onEncodeResult(byte[] h264);
    }

    public void setOnEncodeResultListener(OnEncodeResultListener listener) {
        this.mListener = listener;
    }

    public H264EncodeThread(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        this.mQueue = new ArrayBlockingQueue(5);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void run() {
        if(mEncodeCodec != null) {
            releaseEncodeCodec();
        }
        initEncodeCodec();
        while (!isExit && mEncodeCodec!=null) {
            // 根据下标获取输入缓存区，并向缓存区写入原始帧
            // 然后将缓存区提交给编码器
            if(mEncodeCodec != null) {
                int inputIndex = mEncodeCodec.dequeueInputBuffer(TIMESOUTUS);
                if(inputIndex > 0) {
                    ByteBuffer inputBuffer = mEncodeCodec.getInputBuffer(inputIndex);
                    byte[] rawFrame = new byte[0];
                    try {
                        rawFrame = (byte[]) mQueue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(inputBuffer != null) {
                        inputBuffer.clear();
                        inputBuffer.put(rawFrame);
                        mEncodeCodec.queueInputBuffer(inputIndex, 0 , rawFrame.length, System.nanoTime() / 1000,0);
                    }
                }
            }
            // 根据下标获取输出缓存区，读取编码后数据
            // 有可能存储在几块缓存区
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            byte[] ppsSps = new byte[0];
            int outputIndex;
            do {
                outputIndex = mEncodeCodec.dequeueOutputBuffer(bufferInfo, TIMESOUTUS);
                if(outputIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if(outputIndex >= 0) {
                    ByteBuffer outputBuffer = mEncodeCodec.getOutputBuffer(outputIndex);
                    if(outputBuffer != null) {
                        byte[] outData = new byte[bufferInfo.size];
                        outputBuffer.position(bufferInfo.offset);
                        outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                        outputBuffer.get(outData);
                        // 获取NALU头(1个字节)，判断是否是pps sps
                        // 并将其添加到关键帧前面,在加密关键帧负载
                        // 关键帧的起始码通常为0001,加上NALU头，即5个字节
                        int countStartHeader = 5;
                        int naluType = outputBuffer.get(4) & 0x1F;
                        if(naluType == 7 || naluType == 8) {
                            ppsSps = outData;
                        } else if(naluType == 5) {
                            byte[] iframeData = new byte[ppsSps.length + bufferInfo.size];
                            System.arraycopy(ppsSps, 0 , iframeData, 0, ppsSps.length);
                            System.arraycopy(outData, 0, iframeData, ppsSps.length, outData.length);
                            outData = iframeData;
                        }
                        // 将编码H264数据回调
                        if(mListener != null) {
                            mListener.onEncodeResult(outData);
                        }
                    }
                    mEncodeCodec.releaseOutputBuffer(outputIndex, false);
                }
            }while (outputIndex >= 0 && !isExit);
        }
        releaseEncodeCodec();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initEncodeCodec() {
        MediaFormat mediaFormat;
        if(isVerticalShoot) {
            mediaFormat = MediaFormat.createVideoFormat(MIME, mHeight, mWidth);
        } else {
            // 默认横屏拍摄
            mediaFormat = MediaFormat.createVideoFormat(MIME, mWidth, mHeight);
        }

        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 25);     // 帧率
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, getBitRate());    // 码率
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1); // 关键帧间隔
        mColorFormat = getSupportColorFormat(MIME);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, mColorFormat); // 编码器支持的颜色格式

        try {
            mEncodeCodec = MediaCodec.createEncoderByType(MIME);
            mEncodeCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mEncodeCodec.start();
            isCodecStart = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setFrameData(byte[] rawFrame) {
        if(mQueue != null && isCodecStart) {
            try {
                // 旋转YUV
                rotateYUV(rawFrame, mWidth, mHeight);
                // 颜色格式转换
                // 即匹配编码器支持的颜色格式
                transformColorFormat(rawFrame, mWidth, mHeight);

                mQueue.put(rawFrame);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void transformColorFormat(byte[] rawFrame, int mWidth, int mHeight) {
        if(mColorFormat == MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar) {
            YuvUtils.nativeNV21ToYUV420sp(rawFrame, mWidth, mHeight);
        } else  if(mColorFormat == MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar){
            YuvUtils.nativeNV21ToYUV420p(rawFrame, mWidth, mHeight);
        }
    }

    private void rotateYUV(byte[] rawFrame, int mWidth, int mHeight) {
        // 本例只处理后置摄像头横屏、竖直拍摄
        // 且未处理画面自动校正，需要配合方向传感器
        if(isVerticalShoot) {
            YuvUtils.nativeRotateBackNV21(rawFrame,
                    mWidth, mHeight,
                    YuvUtils.DEGREE_BACK_ROTATE_90);
        }
    }

    public void exit() {
        this.isExit = true;
    }

    private int getBitRate() {
        return (int) (mWidth * mHeight * 20 * 2 * 0.07);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void releaseEncodeCodec() {
        if(mEncodeCodec != null) {
            mEncodeCodec.stop();
            mEncodeCodec.release();
            mEncodeCodec = null;
        }
        isExit = false;
        isCodecStart = false;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private int getSupportColorFormat(String mimeType) {
        MediaCodecList codecList = new MediaCodecList(MediaCodecList.ALL_CODECS);
        MediaCodecInfo[] codecInfos = codecList.getCodecInfos();
        for(MediaCodecInfo codecInfo: codecInfos) {
            // 找到编码器
            if(! codecInfo.isEncoder()) {
                continue;
            }
            // 比对编码器支持的MIME类型是否符合要求
            // 如果找到，再获取对应的颜色格式
            String[] supportTypes = codecInfo.getSupportedTypes();
            for (int i=0; i<supportTypes.length; i++) {
                if(! supportTypes[i].equalsIgnoreCase(mimeType)) {
                    continue;
                }
                MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType(mimeType);
                for(int j=0; j<capabilities.colorFormats.length; j++) {
                    int colorFormat = capabilities.colorFormats[j];
                    if(isCodecRecoginzedFormat(colorFormat)) {
                        return colorFormat;
                    }
                }
            }
        }
        return 0;
    }


    private boolean isCodecRecoginzedFormat(int colorFormat) {
        switch (colorFormat) {
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
                return true;
        }
        return false;
    }

    // YYYYYYYY VUVU  --> YYYYYYYY UVUV
    // 将NV21转换为Yuv420sp
    public byte[] nv21ToYuv420sp(byte[] src) {
        int yLength = mWidth * mHeight;
        int uLength = yLength / 4;
        int vLength = yLength / 4;
        int frameSize = yLength + uLength + vLength;
        byte[] yuv420sp = new byte[frameSize];
        // Y分量
        System.arraycopy(src, 0, yuv420sp, 0, yLength);
        for (int i = 0; i < yLength/4; i++) {
            // U分量
            yuv420sp[yLength + 2 * i] = src[yLength + 2*i+1];
            // V分量
            yuv420sp[yLength + 2*i+1] = src[yLength + 2*i];
        }
        return yuv420sp;
    }
}
