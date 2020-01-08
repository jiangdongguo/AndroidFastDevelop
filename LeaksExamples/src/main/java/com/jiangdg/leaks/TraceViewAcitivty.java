package com.jiangdg.leaks;

import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @Auther: Jiangdg
 * @Date: 2019/11/15 16:11
 * @Description: TraceView分析案例
 */
public class TraceViewAcitivty extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

  //      Debug.startMethodTracing("test");
    }

    private void initView() {
        // 模拟耗时操作，阻塞主线程
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

 //       Debug.stopMethodTracing();
    }
}
