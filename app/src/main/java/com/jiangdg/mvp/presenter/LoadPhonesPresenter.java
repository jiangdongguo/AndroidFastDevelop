package com.jiangdg.mvp.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.jiangdg.mvp.bean.PhoneInfoBean;
import com.jiangdg.mvp.model.ILoadPhonesModel;
import com.jiangdg.mvp.model.LoadPhonesModelImpl;
import com.jiangdg.mvp.view.ILoadPhonesView;

import java.util.List;

/**加载手机数据P层
 *
 * Created by jianddongguo on 2017/6/29.
 * http://blog.csdn.net/andrexpert
 */

public class LoadPhonesPresenter extends BasePresenter<ILoadPhonesView> {
    private static final int TYPE_MSG_LOADING = 1;
    //M层接口
    private ILoadPhonesModel mLoadPhoneModel;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TYPE_MSG_LOADING:
                    String result = msg.getData().getString("result");
                    if("下载成功".equals(result)){
                        // 将加载结果传递到V层
                        List<PhoneInfoBean> data = (List<PhoneInfoBean>) msg.obj;
                        if(data != null) {
                            getView().showToast("下载数据成功");
                            getView().showPhoneInfos(data);
                        }
                    }else if("下载失败".equals(result)){
                        getView().showToast("下载数据失败");
                    }
                default:
                    break;
            }
        }
    };

    protected void sendMessage(int what,Object obj,String message){
        Message msg = new Message();
        msg.obj = obj;
        msg.what = what;
        Bundle bundle =new Bundle();
        bundle.putString("result",message);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }


    public LoadPhonesPresenter(){
        this.mLoadPhoneModel = new LoadPhonesModelImpl();
    }

    public void fetchData(){
        if(getView() == null || mLoadPhoneModel == null){
            return;
        }
        mLoadPhoneModel.loadPhones(new ILoadPhonesModel.PhonesOnLoadListener() {
            @Override
            public void onComplete(List<PhoneInfoBean> data) {
                if(data != null){
                    sendMessage(TYPE_MSG_LOADING,data,"下载成功");
                }else{
                    sendMessage(TYPE_MSG_LOADING,null,"下载失败");
                }
            }
        });
    }
}
