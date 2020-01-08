package com.jiangdg.floatball;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private boolean isShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBtnClick(View view) {
        if(! isShow) {
            startService(new Intent(this, FloatBallService.class));
        } else {
            stopService(new Intent(this, FloatBallService.class));
        }
        isShow = !isShow;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
