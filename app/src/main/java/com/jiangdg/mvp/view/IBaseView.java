package com.jiangdg.mvp.view;

/** MVP框架V层，基抽象接口
 * Created by jianddongguo on 2017/7/5.
 * http://blog.csdn.net/andrexpert
 */

public interface IBaseView {
    // 显示进度框
    void showProgress(String content);

    // 取消进度框
    void dismissProgress();

    // 打印toast
    void showToast(String msg);
}
