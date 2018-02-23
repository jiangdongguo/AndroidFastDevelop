package com.jiangdg.mvp.application;

import android.app.Application;

import com.jiangdg.mvp.common.CrashManager;

/**全局类，监听异常
 * Created by jianddongguo on 2018/2/23.
 */

public class MVPApplication extends Application {
    private CrashManager mExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
