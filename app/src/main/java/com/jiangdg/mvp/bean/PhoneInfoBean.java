package com.jiangdg.mvp.bean;

/** 手机信息实体
 * Created by jianddongguo on 2017/6/29.
 * http://blog.csdn.net/andrexpert
 */

public class PhoneInfoBean {
    private int phonePic;
    private String phoneName;
    private int phonePrice;

    public PhoneInfoBean(int phonePic, String phoneName, int phonePrice) {
        this.phonePic = phonePic;
        this.phoneName = phoneName;
        this.phonePrice = phonePrice;
    }

    public int getPhonePic() {
        return phonePic;
    }

    public void setPhonePic(int phonePic) {
        this.phonePic = phonePic;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public long getPhonePrice() {
        return phonePrice;
    }

    public void setPhonePrice(int phonePrice) {
        this.phonePrice = phonePrice;
    }
}
