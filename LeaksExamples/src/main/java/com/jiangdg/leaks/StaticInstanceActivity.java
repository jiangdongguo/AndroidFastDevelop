package com.jiangdg.leaks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**非静态内部类创建静态实例造成的内存泄漏
 * @Auther: Jiangdg
 * @Date: 2019/10/9 10:43
 * @Description:
 */
public class StaticInstanceActivity extends AppCompatActivity {
    private static SomeResources mSomeResources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mSomeResources == null) {
            // 优化，使用Application的Context
            mSomeResources = new SomeResources(this.getApplicationContext());
        }
    }

    // 优化：使用静态内部类
    static class SomeResources {
        private Context mCtx;

        public SomeResources(Context context) {
            this.mCtx = context;
        }
    }
}
