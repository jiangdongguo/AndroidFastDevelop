package com.jiangdg.mvp.utils.common;

import android.os.Environment;

/**常量
 * Created by jiangdongguo on 2018/2/24.
 */

public class ConstantUtil {
    public static final boolean DEBUG = true;
    public static final String LOGPATH = Environment.getExternalStorageDirectory()+
            "/log.txt";

    public interface CacheData {
        String TIP_CRASH = "糟糕!程序异常，即将重启...";
    }
}
