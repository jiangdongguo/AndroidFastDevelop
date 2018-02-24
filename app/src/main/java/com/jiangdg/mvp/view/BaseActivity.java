package com.jiangdg.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jiangdg.mvp.presenter.BasePresenter;
import com.jiangdg.mvp.utils.common.ActivityStackManager;

/**Activity基类
 *
 * Created by jianddongguo on 2017/7/5.
 * http://blog.csdn.net/andrexpert
 */

public abstract class BaseActivity<V,T extends BasePresenter<V>> extends AppCompatActivity {
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 创建presenter
        mPresenter = createPresenter();
        // 绑定view
        mPresenter.attachView((V)this);
        // Activity入栈
        ActivityStackManager.getInstance().pushActivity(this);
    }

    protected abstract T createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.dettachView();
        }
        // Activity出栈
        ActivityStackManager.getInstance().popActivity();
    }
}
