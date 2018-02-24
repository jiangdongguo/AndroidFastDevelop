package com.jiangdg.mvp.application;

import android.app.Application;
import android.os.Environment;

import com.jiangdg.mvp.utils.common.CrashManager;

/**全局类，继承于Application
 * Created by jianddongguo on 2018/2/23.
 */

public class MvpApplication extends Application {
    private CrashManager mCrashManager;

    @Override
    public void onCreate() {
        super.onCreate();
        // 全局异常捕获
        mCrashManager = CrashManager.getInstance();
        mCrashManager.initCrashHandler(this);
    }
}
