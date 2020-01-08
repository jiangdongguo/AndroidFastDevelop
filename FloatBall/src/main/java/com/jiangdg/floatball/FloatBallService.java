package com.jiangdg.floatball;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 *  悬浮球后台服务
 *
 * @author Jiangdg
 * @since 2019-08-09 09:14:00
 * */
public class FloatBallService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FloatBallManager.getInstance(this).createFloatView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FloatBallManager.getInstance(this).removeFloatView();
    }
}
