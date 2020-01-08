package com.jiangdg.leaks.tools;

import android.content.Context;

/** 工具类，单例模式
 * @Auther: Jiangdg
 * @Date: 2019/10/8 17:23
 * @Description:
 */
public class CommonUtils {
    private static CommonUtils instance;
    private Context mCtx;

    private CommonUtils(Context context){
        this.mCtx = context;
    }

    public static CommonUtils getInstance(Context context) {
        if(instance == null) {
            instance = new CommonUtils(context);
        }
        return instance;
    }
}
