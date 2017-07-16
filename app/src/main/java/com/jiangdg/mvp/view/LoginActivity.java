package com.jiangdg.mvp.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jiangdg.mvp.R;
import com.jiangdg.mvp.bean.UserInfoBean;
import com.jiangdg.mvp.presenter.LoginPresenter;

/** 用户登录界面
 *
 * Created by jianddongguo on 2017/6/29.
 * http://blog.csdn.net/andrexpert
 */
public class LoginActivity extends BaseActivity<ILoginView,LoginPresenter> implements ILoginView{
    private ProgressDialog mpDialog;
    private EditText mEdtUserName;
    private EditText mEdtPasswd;
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    private void initView() {
        mEdtUserName = (EditText)findViewById(R.id.etv_usrname);
        mEdtPasswd = (EditText)findViewById(R.id.etv_passwd);
        mBtnLogin = (Button)findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPresenter != null){
                    mPresenter.toLogin();
                }
            }
        });
    }

    @Override
    public void showProgress(String msg) {
        mpDialog = ProgressDialog.show(LoginActivity.this,"提示",msg);
    }

    @Override
    public void dismissProgress() {
        if(mpDialog != null){
            mpDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public UserInfoBean getLoginData() {
        UserInfoBean bean = new UserInfoBean();
        bean.setUserName(mEdtUserName.getText().toString());
        bean.setUserPasswd(mEdtPasswd.getText().toString());
        return bean;
    }

    @Override
    public void loginResult(boolean isSuccess) {
        if(isSuccess){
            Intent intent = new Intent(LoginActivity.this,LoadPhonesActivity.class);
            startActivity(intent);
        }
    }
}
