package com.jiangdg.mvp.presenter;

import android.view.View;

import com.jiangdg.mvp.view.IBaseView;

import java.lang.ref.WeakReference;

/**MVP框架P层基类，使用弱引用绑定解绑View
 *
 * Created by jianddongguo on 2017/7/5.
 * http://blog.csdn.net/andrexpert
 */

public  class BasePresenter<V> {
    //View的引用
    protected WeakReference<V> mViewRef;

    //绑定View
    public void attachView(V view){
        mViewRef = new WeakReference<V>(view);
    }

    //解绑View
    public void dettachView(){
        if(mViewRef != null){
            mViewRef.clear();
        }
    }

    // 向子类暴露View的引用
    protected V getView(){
        return mViewRef.get();
    }
}
