package com.jiangdg.mvp.view;

import com.jiangdg.mvp.bean.PhoneInfoBean;

import java.util.List;

/** LoadFruitsActivityUI业务逻辑接口
 *
 * Created by jianddongguo on 2017/6/29.
 * http://blog.csdn.net/andrexpert
 */

public interface ILoadPhonesView extends IBaseView{

    // 显示手机数据到RecyclerView
    void showPhoneInfos(List<PhoneInfoBean> data);
}
