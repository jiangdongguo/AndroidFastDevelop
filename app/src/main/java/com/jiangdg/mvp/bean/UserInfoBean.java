package com.jiangdg.mvp.bean;

/**用户登录信息
 *
 * Created by jianddongguo on 2017/7/3.
 */

public class UserInfoBean {
    private String userName;
    private String userPasswd;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPasswd() {
        return userPasswd;
    }

    public void setUserPasswd(String userPasswd) {
        this.userPasswd = userPasswd;
    }
}
