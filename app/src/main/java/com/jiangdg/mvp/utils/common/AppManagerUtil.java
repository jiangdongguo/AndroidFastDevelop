package com.jiangdg.mvp.utils.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * APP管理工具类
 * Created by jiangdongguo on 2018/2/24.
 */

public class AppManagerUtil {

    public static void restartApp(Context ctx) {
        PackageManager pckgManager = ctx.getApplicationContext().getPackageManager();
        Intent intent = pckgManager.getLaunchIntentForPackage(ctx.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx.getApplicationContext()
                , 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager manager = (AlarmManager) ctx.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, pendingIntent);
    }

    public static void releaseAppResource() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public static void removeAllActivity() {
        ActivityStackManager.getInstance().popAllActivity();
    }

    public static boolean isNetworkAvailbale() {
        return false;
    }
}
