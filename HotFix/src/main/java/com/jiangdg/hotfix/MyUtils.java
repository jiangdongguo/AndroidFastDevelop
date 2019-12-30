package com.jiangdg.hotfix;

/** 制造bug，即要修复的地方
 * author : jiangdg
 * date   : 2019/12/29 14:51
 * desc   :
 * version: 1.0
 */
public class MyUtils {

    public static int divisionOperate() {
        int a = 15;
        // 原始bug代码
        int b = 0;
        // 修改后的代码
//        int b=3;
        return a/b;
    }
}
