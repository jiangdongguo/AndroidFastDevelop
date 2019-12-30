package com.jiangdg.hotfix;

import android.app.Application;

import com.jiangdg.hotfix.hotfix.HotFix;

import java.io.File;

/** 全局Application
 * author : jiangdg
 * date   : 2019/12/29 13:57
 * desc   : 使用HotFix框架
 * version: 1.0
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        hotFixBug();
    }

    private void hotFixBug() {
        File file = new File("sdcard/patch.jar");
        if(! file.exists()) {
            return;
        }
        HotFix.fix(this, file);
    }
}
