package com.jiangdg.leaks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jiangdg.leaks.tools.CommonUtils;

/**使用单例模式时造成内存泄漏
 *
 * @Auther: Jiangdg
 * @Date: 2019/10/8 17:24
 * @Description:
 */
public class SingleInstanceActivity extends AppCompatActivity {
    private CommonUtils mUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_instance);

        // 造成内存泄漏
        mUtils = CommonUtils.getInstance(this);
        // 优化：使用Application的Context
//        mUtils = CommonUtils.getInstance(this.getApplicationContext());
    }
}
