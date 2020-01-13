package com.jiangdg.threadpool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleThreadPool threadPool = new SimpleThreadPool(5, new LinkedBlockingQueue<Runnable>(10));
        // 创建20个任务，丢入线程池中执行
        for(int i=0; i<20; i++) {
            final int num = i;
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Log.d("threadpool", "我是"+ Thread.currentThread().getName()+ "线程"+ ",正在执行第"+num+"个任务");
                }
            });
        }
    }
}
