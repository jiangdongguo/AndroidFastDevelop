package com.jiangdg.mvp.model;

import com.jiangdg.mvp.R;
import com.jiangdg.mvp.bean.PhoneInfoBean;

import java.util.ArrayList;
import java.util.List;

/** 加载手机数据M层，处理数据获取业务逻辑
 *
 * Created by jianddongguo on 2017/6/29.
 * http://blog.csdn.net/andrexpert
 */

public class LoadPhonesModelImpl implements ILoadPhonesModel {

    @Override
    public void loadPhones(final PhonesOnLoadListener listener) {
        // 模拟请求服务器
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 延时2s
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 请求数据
                List<PhoneInfoBean> data = new ArrayList<>();
                PhoneInfoBean item1 = new PhoneInfoBean(R.mipmap.aa,"锤子手机，一吹定终生....",2000);
                PhoneInfoBean item2 = new PhoneInfoBean(R.mipmap.bb,"金立S10，四圈拍照更美！哈哈哈哈",2500);
                PhoneInfoBean item3 = new PhoneInfoBean(R.mipmap.cc,"华为荣耀6x，我是性价比之王，你值得拥有",1400);
                PhoneInfoBean item4 = new PhoneInfoBean(R.mipmap.dd,"华为v10，高档品质，奢华享受",2400);
                PhoneInfoBean item5 = new PhoneInfoBean(R.mipmap.aa,"锤子手机，一吹定终生....",2000);
                PhoneInfoBean item6 = new PhoneInfoBean(R.mipmap.bb,"金立S10，四圈拍照更美！哈哈哈哈",2500);
                PhoneInfoBean item7 = new PhoneInfoBean(R.mipmap.cc,"华为荣耀6x，我是性价比之王，你值得拥有",1400);

                data.add(item1);
                data.add(item2);
                data.add(item3);
                data.add(item4);
                data.add(item5);
                data.add(item6);
                data.add(item7);
                // 回调结果
                listener.onComplete(data);
            }
        }).start();
    }
}
