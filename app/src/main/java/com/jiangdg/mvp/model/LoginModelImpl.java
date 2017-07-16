package com.jiangdg.mvp.model;

/**模拟请求服务器用户登录
 *
 * Created by jianddongguo on 2017/6/29.
 */

public class LoginModelImpl implements ILoginModel {
    private static final String name = "jiangdongguo";
    private static final String passwd = "123";

    @Override
    public void verifyUserPswd(final String usrName,final String usrPasswd,final verifyOnResultListener listener) {
        // 耗时任务，在子线程中进行
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 延时3s用于模拟请求服务器登录
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(name.equals(usrName) && passwd.equals(usrPasswd)) {
                    //验证成功
                    listener.onSuccess();
                }else{
                    // 验证失败
                    listener.onFailure();
                }
            }
        }).start();
    }

}
