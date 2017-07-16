package com.jiangdg.mvp.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.jiangdg.mvp.bean.UserInfoBean;
import com.jiangdg.mvp.model.ILoginModel;
import com.jiangdg.mvp.model.LoginModelImpl;
import com.jiangdg.mvp.view.ILoginView;

/**用户登录P层
 *
 * Created by jianddongguo on 2017/6/29.
 * http://blog.csdn.net/andrexpert
 */

public class LoginPresenter extends BasePresenter<ILoginView> {
    private static final int TYPE_MSG_LOGIN = 0;
    // M层接口
    private ILoginModel mLoginModel;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TYPE_MSG_LOGIN:
                    String result = msg.getData().getString("result");
                    if("登录成功".equals(result)){
                        getView().showToast("登录成功");
                    }else if("登录失败".equals(result)){
                        getView().showToast("登录失败");
                    }
                    getView().dismissProgress();
                default:
                    break;
            }
        }
    };

    public LoginPresenter(){
        this.mLoginModel = new LoginModelImpl();
    }

    public void toLogin(){
        String usrName = null;
        String usrPasswd = null;
        if(getView() == null || mLoginModel == null){
            return;
        }
        UserInfoBean bean = getView().getLoginData();
        if(bean != null){
            usrName = bean.getUserName();
            usrPasswd = bean.getUserPasswd();
            if(TextUtils.isEmpty(usrName) || TextUtils.isEmpty(usrPasswd)){
                getView().showToast("用户名或密码不能为空");
                return;
            }
        }
        getView().showProgress("正在登录，请稍后...");
        mLoginModel.verifyUserPswd(usrName, usrPasswd, new ILoginModel.verifyOnResultListener() {
            @Override
            public void onSuccess() {
                getView().loginResult(true);
                sendMessage(TYPE_MSG_LOGIN,"登录成功");
            }

            @Override
            public void onFailure() {
                getView().loginResult(false);
                sendMessage(TYPE_MSG_LOGIN,"登录失败");
            }
        });
    }

    private void sendMessage(int what,String message){
        Message msg = new Message();
        msg.what = what;
        Bundle bundle = new Bundle();
        bundle.putString("result",message);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }
}
