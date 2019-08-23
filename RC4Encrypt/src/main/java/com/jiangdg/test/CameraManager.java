package com.jiangdg.test;


import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.io.IOException;
import java.lang.ref.WeakReference;

/** Camera管理类
 *
 * @author Jiangdg
 * @since 2019-08-22 9:22
 * */
public class CameraManager {
    public static final int previewWidth = 640;
    public static final int previewHeight = 480;
    private static final int PREVIEW_ROTATION_VERTICAL = 90;
    private static CameraManager mManager;
    private Camera mCamera;
    private WeakReference<Activity> mActivityWf;
    private boolean isFrontCamera;

    private CameraManager(Activity activity) {
        mActivityWf = new WeakReference<>(activity);
    }

    public static CameraManager getInstance(Activity activity) {
        if(mManager == null) {
            synchronized (CameraManager.class) {
                if(mManager == null) {
                    mManager = new CameraManager(activity);
                }
            }
        }
        return mManager;
    }

    public void openCamera(Camera.PreviewCallback previewCallBack) {
        if(isFrontCamera) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            int numCameras = Camera.getNumberOfCameras();
            for(int i=0; i<numCameras; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    mCamera = Camera.open(i);
                }
            }
        } else {
            mCamera = Camera.open();
        }

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(previewWidth, previewHeight);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.setPreviewFormat(ImageFormat.NV21);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(getPreviewOrientation());
        mCamera.setPreviewCallback(previewCallBack);
    }

    private int getPreviewOrientation() {
        // 判断设备手持方向
        // 正垂直为0，按顺时针依次为90、180、270
        int degrees = 0;
        if(mActivityWf != null) {
            WindowManager manager = mActivityWf.get().getWindowManager();
            if(manager == null) {
                return PREVIEW_ROTATION_VERTICAL;
            }
            int rotation = manager.getDefaultDisplay().getRotation();
            switch (rotation) {
                case Surface.ROTATION_0: degrees = 0; break;
                case Surface.ROTATION_90: degrees = 90; break;
                case Surface.ROTATION_180: degrees = 180; break;
                case Surface.ROTATION_270: degrees = 270; break;
            }
        }
        // 调整Camera预览角度
        int result;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraId = isFrontCamera?Camera.CameraInfo.CAMERA_FACING_FRONT:Camera.CameraInfo.CAMERA_FACING_BACK;
        Camera.getCameraInfo(cameraId, cameraInfo);
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
              result = (cameraInfo.orientation + degrees) % 360;
              result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
              result = (cameraInfo.orientation - degrees + 360) % 360;
          }
        return result;
    }

    public void startPreview(SurfaceHolder holder) {
        if(mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
        }
    }

    public void stopPreview() {
        if(mCamera != null) {
            mCamera.stopPreview();
            try {
                mCamera.setPreviewDisplay(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeCamera() {
        if(mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        if(mActivityWf != null) {
            mActivityWf.clear();
        }
    }
}
