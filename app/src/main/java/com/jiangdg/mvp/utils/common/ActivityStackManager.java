package com.jiangdg.mvp.utils.common;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;

/**
 * Activity栈管理类
 * Created by jianddongguo on 2018/2/23.
 */

public class ActivityStackManager {
    private final String TAG = ActivityStackManager.this.getClass().getName();
    // Activity栈
    private Stack<Activity> mStack = new Stack<>();
    // 同步锁
    private static Object object = new Object();

    private static ActivityStackManager mManager;

    private ActivityStackManager() {
    }

    public static ActivityStackManager getInstance() {
        synchronized (object) {
            if (mManager == null) {
                mManager = new ActivityStackManager();
            }
            return mManager;
        }
    }

    // 入栈操作
    public void pushActivity(Activity activity) {
        if (mStack != null) {
            mStack.push(activity);
            if (ConstantUtil.DEBUG)
                Log.d(TAG, activity.getClass().getName() + "入栈");
        }
    }

    // 出栈(栈顶元素)操作
    public void popActivity() {
        if (mStack != null && !mStack.empty()) {
            Activity activity = mStack.pop();
            activity.finish();
            if (ConstantUtil.DEBUG)
                Log.d(TAG, activity.getClass().getName() + "出栈");
        }
    }

    // 出栈(指定元素)操作
    public void removeActivity(Activity activity) {
        if (mStack != null && !mStack.empty()) {
            mStack.remove(activity);
            if (ConstantUtil.DEBUG)
                Log.d(TAG, activity.getClass().getName() + "出栈");
        }
    }

    public Activity getStackTop() {
        Activity activity = null;
        if (mStack != null && !mStack.empty()) {
            activity = mStack.peek();
            if (ConstantUtil.DEBUG)
                Log.d(TAG, "栈顶Activity：" + activity.getClass().getName());
        }
        return activity;
    }

    // 出栈所有Activity
    public void popAllActivity() {
        if (mStack != null && !mStack.empty()) {
            for (int i = 0; i < mStack.size(); i++) {
                popActivity();
            }
        }
    }
}
