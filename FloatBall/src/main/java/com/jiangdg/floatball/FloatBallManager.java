package com.jiangdg.floatball;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 *  悬浮球管理类
 *      悬浮权限检查
 *      自动吸边
 *
 * @author Jiangdg
 * @since 2019-08-09 09:14:00
 * */
public class FloatBallManager {
    private static final int HIDE_VIEW_IN_SECS = 3000;
    private static FloatBallManager mManager;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mBallParams;
    private FloatBallView mFloatView;
    private Context mCtx;
    private Runnable mRunAction;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        private float tempY;
        private float tempX;
        private float startX;
        private float startY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getRawX();
                    startY = event.getRawY();
                    tempX = event.getRawX();
                    tempY = event.getRawY();
                    if(mRunAction != null) {
                        mFloatView.removeCallbacks(mRunAction);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dx = event.getRawX() - startX;
                    float dy = event.getRawY() - startY;
                    // 计算偏移量，刷新视图
                    mBallParams.x += dx;
                    mBallParams.y += dy;
                    mWindowManager.updateViewLayout(mFloatView, mBallParams);
                    // 更新坐标
                    startX = event.getRawX();
                    startY = event.getRawY();
                    Log.d("ddddd", "dx="+dx+";dy="+dy);
                    break;
                case MotionEvent.ACTION_UP:
                    float endX = event.getRawX();
                    float endY = event.getRawY();
                    final float tmpEndX = endX;
                    final float tmpEndY = endY;
                    Log.d("ddddd", "tmpEndX - tempX="+(tmpEndX - tempX)+";tmpEndY - tempY="+(tmpEndY - tempY));
                    // 3秒后自动隐藏一半
                    mRunAction = new Runnable() {
                        @Override
                        public void run() {
                            if(tmpEndX < getScreenWidth() / 2) {
                                mBallParams.x = -mFloatView.getWidth()/2;
                                mWindowManager.updateViewLayout(mFloatView, mBallParams);
                            } else {
                                mBallParams.x = (int)getScreenWidth()- mFloatView.getWidth()/2;
                                mWindowManager.updateViewLayout(mFloatView, mBallParams);
                            }
                        }
                    };
                    mFloatView.postDelayed(mRunAction, HIDE_VIEW_IN_SECS);
                    // 自动贴边，这里需要限定endY最大小值，不能越出手机屏幕
                    if(endX < getScreenWidth() / 2) {
                        endX = 0;
                        if(endY <= 0) {
                            endY = 0;
                        } else if(endY >= getScreenHeight()-2*mFloatView.getHeight()) {
                            endY =  getScreenHeight()-2*mFloatView.getHeight();
                        }
                    } else {
                        endX = getScreenWidth() - mFloatView.getWidth();
                        if(endY <= 0) {
                            endY = 0;
                        } else if(endY >= getScreenHeight()-2*mFloatView.getHeight()) {
                            endY =  getScreenHeight()-2*mFloatView.getHeight();
                        }
                    }
                    // 判断是否需要拦截事件，拦截规则为本次移动是否超过10个像素
                    // 如果超过则拦截事件并认定为滑动，否则不拦截以将事件交给onClick处理
                    if(Math.abs(tmpEndX - tempX) > 10 || Math.abs(tmpEndY - tempY) > 10) {
                        mBallParams.x = (int)endX;
                        mBallParams.y = (int)endY;
                        mWindowManager.updateViewLayout(mFloatView, mBallParams);
                        return true;
                    }
                    break;
            }
            return false;
        }
    };

    private FloatBallManager(Context ctx){
        this.mCtx = ctx;
        init();
    }

    public static FloatBallManager getInstance(Context ctx) {
        if(mManager == null) {
            synchronized (FloatBallManager.class) {
                if(mManager == null) {
                    mManager = new FloatBallManager(ctx.getApplicationContext());
                }
            }
        }
        return mManager;
    }

    private void init() {
        mWindowManager = (WindowManager) mCtx.getSystemService(Context.WINDOW_SERVICE);
        mFloatView = new FloatBallView(mCtx);
        mFloatView.setOnTouchListener(mTouchListener);
    }

    public void createFloatView() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(mCtx)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:"+mCtx.getPackageName()));
            mCtx.startActivity(intent);
        }else{
            mBallParams = new WindowManager.LayoutParams();
            mBallParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            mBallParams.flags = mBallParams.flags | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            mBallParams.flags = mBallParams.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
            mBallParams.alpha = 1.0f;
            mBallParams.gravity = Gravity.START | Gravity.TOP;
            mBallParams.format = PixelFormat.RGBA_8888;
            mBallParams.width = mFloatView.getWidth();
            mBallParams.height = mFloatView.getHeight();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBallParams.type =  WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                mBallParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
            mWindowManager.addView(mFloatView, mBallParams);
        }
    }

    public void removeFloatView() {
        if(mWindowManager != null) {
            mWindowManager.removeView(mFloatView);
        }
    }

    private float getScreenWidth() {
        Point point = new Point();
        mWindowManager.getDefaultDisplay().getSize(point);
        return point.x;
    }

    private float getScreenHeight() {
        Point point = new Point();
        mWindowManager.getDefaultDisplay().getSize(point);
        return point.y;
    }
}
