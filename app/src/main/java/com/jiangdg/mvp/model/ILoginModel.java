package com.jiangdg.mvp.model;

/**用户登录M层接口
 *
 * Created by jianddongguo on 2017/6/29.
 */

public interface ILoginModel {

    // 网络请求，验证用户密码
    void verifyUserPswd(String usrName,String passwd,verifyOnResultListener listener);

    // 回调接口:将请求结果暴露给调用者
    public interface verifyOnResultListener{
        void onSuccess();

        void onFailure();
    }
}
