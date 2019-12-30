package com.jiangdg.hotfix;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * author : jiangdg
 * date   : 2019/12/29 14:51
 * desc   :
 * version: 1.0
 */
public class ExampleActivity extends AppCompatActivity {

    private static final String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(checkAndRequestPermissions(permissions, 0, true)) {
            Toast.makeText(ExampleActivity.this, "a/b="+MyUtils.divisionOperate(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 获取授权失败的权限列表
        List<String> denyPermissions = new ArrayList<>();
        for(int i=0; i<grantResults.length; i++) {
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(permissions[i]);
            }
        }

        // 根据授权情况，作处理
        if(denyPermissions.size()==0) {
            Toast.makeText(ExampleActivity.this, "a/b="+MyUtils.divisionOperate(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ExampleActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
        }
    }


    protected boolean checkAndRequestPermissions(String[] permissions, int questCode, boolean isRequestPermission) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if(permissions == null) {
            throw new NullPointerException("permissions array can not be null.");
        }
        // 获取未被授权权限列表
        List<String> denyPermissions = new ArrayList<>();
        for (int i=0 ; i<permissions.length;i++) {
            if(PackageManager.PERMISSION_GRANTED != checkSelfPermission(permissions[i])) {
                denyPermissions.add(permissions[i]);
            }
        }
        // 请求未被授权的权限
        if(denyPermissions.size() == 0) {
            return true;
        }
        // 是否需要请求权限
        if(isRequestPermission) {
            requestPermissions(denyPermissions.toArray(new String[denyPermissions.size()]), questCode);
        }

        return false;
    }
}

