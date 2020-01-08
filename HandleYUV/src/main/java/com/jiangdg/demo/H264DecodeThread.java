package com.jiangdg.demo;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *  解码线程
 *
 * @author Jiangdg
 * @since 2019-08-22 11:30
 * */
public class H264DecodeThread extends Thread {
    private static final String MIME_H264 = "video/avc";
    private static final long TIMEOUTUS = 10000;
    private MediaCodec mDecodeCodec;
    private int mWidth;
    private int mHeight;
    private ArrayBlockingQueue mQueue;
    private boolean isExit;
    private Surface mSurface;

    public H264DecodeThread(int mWidth, int mHeight) {
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.mQueue = new ArrayBlockingQueue(5);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void run() {
        if(mDecodeCodec != null) {
            releaseDecodeCodec();
        }
        initDecodeCodec();

        while (! isExit && mDecodeCodec != null) {
            // 读入编码数据
            int inputIndex = mDecodeCodec.dequeueInputBuffer(TIMEOUTUS);
            if(inputIndex >= 0) {
                ByteBuffer inputBuffer = mDecodeCodec.getInputBuffer(inputIndex);
                byte[] h264 = new byte[0];
                if(inputBuffer != null) {
                    try {
                        h264 = (byte[]) mQueue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(h264.length > 0) {
                        inputBuffer.clear();
                        inputBuffer.put(h264);
                    }
                }
                mDecodeCodec.queueInputBuffer(inputIndex, 0, h264.length, System.nanoTime()/1000, 0);
            }
            // 读出解码数据
            // 注意releaseOutputBuffer时，render字段为true
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outputIndex;
            do {
                outputIndex = mDecodeCodec.dequeueOutputBuffer(bufferInfo, TIMEOUTUS);
                if(outputIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if(outputIndex > 0) {
                    mDecodeCodec.releaseOutputBuffer(outputIndex, true);
                }
            }while (outputIndex>=0 && !isExit);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initDecodeCodec() {
        if(mSurface == null) {
            throw new IllegalStateException("please call setRenderSurface() before start thread.");
        }
        MediaFormat mediaFormat = MediaFormat.createVideoFormat(MIME_H264, mWidth, mHeight);
        try {
            mDecodeCodec = MediaCodec.createDecoderByType(MIME_H264);
            mDecodeCodec.configure(mediaFormat, mSurface, null, 0);
            mDecodeCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDecodeData(byte[] h264) {
        if(mQueue != null) {
            try {
                mQueue.put(h264);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void exit() {
        isExit = true;
    }

    public void setRenderSurface(Surface surface) {
        this.mSurface = surface;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void releaseDecodeCodec() {
        if(mDecodeCodec != null) {
            mDecodeCodec.stop();
            mDecodeCodec.release();
            mDecodeCodec = null;
        }
    }
}
