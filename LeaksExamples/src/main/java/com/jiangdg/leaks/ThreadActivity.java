package com.jiangdg.leaks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/** 使用线程造成的内存泄漏
 * @Auther: Jiangdg
 * @Date: 2019/10/9 10:04
 * @Description:
 */
public class ThreadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 开启一个子线程
        new Thread(new MyRunnable()).start();
        // 开启一个异步任务
        // 优化：使用Application的Context
        new MyAsyncTask(this.getApplicationContext()).execute();
    }

    // 优化：使用静态内部类
    static class MyRunnable implements Runnable {

        @Override
        public void run() {

        }
    }

    // 优化：使用静态内部类
    // 如果需要传入Context，使用Application的Context
    static class MyAsyncTask extends AsyncTask {
        private Context mCtx;

        public MyAsyncTask(Context context) {
            this.mCtx = context;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }
    }
}
