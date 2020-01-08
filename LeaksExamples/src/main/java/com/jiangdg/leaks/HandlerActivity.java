package com.jiangdg.leaks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

/** 使用Handler造成内存泄漏
 * @Auther: Jiangdg
 * @Date: 2019/10/8 17:55
 * @Description:
 */
public class HandlerActivity extends AppCompatActivity {

// 匿名内部类默认持有HandlerActivity的引用
// 造成内存泄漏
//    private Handler mUIHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//    };

    // 优化，使用静态内部类
    // 假如要持有HandlerActivity，使用弱引用
    private UIHandler mUIHandler;
    static class UIHandler extends Handler {
        private WeakReference<HandlerActivity> mWfActivity;

        public UIHandler(HandlerActivity activity) {
            mWfActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUIHandler = new UIHandler(this);
    }
}
