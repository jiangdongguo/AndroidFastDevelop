package com.jiangdg.mvp.view;

import com.jiangdg.mvp.bean.UserInfoBean;

import java.util.Map;

/** LoginActivity UI业务逻辑接口
 *
 * Created by jianddongguo on 2017/6/29.
 * http://blog.csdn.net/andrexpert
 */

public interface ILoginView extends IBaseView{
    // 获得用户登录输入数据
    UserInfoBean getLoginData();

    void loginResult(boolean isSuccess);
}
