package com.jiangdg.mvp.model;

import com.jiangdg.mvp.bean.PhoneInfoBean;

import java.util.List;

/**加载手机数据M层接口，处理获取数据业务业务逻辑
 *
 * Created by jianddongguo on 2017/6/29.
 * http://blog.csdn.net/andrexpert
 */

public interface ILoadPhonesModel {

    // 获取手机数据业务逻辑
    void loadPhones(PhonesOnLoadListener listener);

    // 回调接口:将获取结果暴露给调用者
    public interface PhonesOnLoadListener{
        void onComplete(List<PhoneInfoBean> data);
    }
}
