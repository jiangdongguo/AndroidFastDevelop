package com.jiangdg.hotfix.hotfix;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.jiangdg.hotfix.hotfix.androidN.NewClassLoaderInjector;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/** 自定义热修复框架
 * author : jiangdg
 * date   : 2019/12/29 13:53
 * desc   : 基于multidex方案
 * version: 1.0
 */
public class HotFix {
    private static final String TAG = HotFix.class.getSimpleName();

    public static void fix(Application context, File patchFile) {
        if(! patchFile.exists()) {
            return;
        }
        Log.d(TAG, "patchPath=" + patchFile.getAbsolutePath());
        ArrayList<File> patches = new ArrayList<>();
        patches.add(patchFile);
        ArrayList<IOException> suppressedExceptions = new ArrayList<IOException>();
        File mOdexDirectory = context.getDir("odex", Context.MODE_PRIVATE);

        // 1. 获取当前应用的PathClassLoader，这个当前应用的类加载器；
        ClassLoader pathClassLoader = context.getClassLoader();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                pathClassLoader = NewClassLoaderInjector.inject(context, pathClassLoader);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        // 2. 通过反射获得DexPathList属性对象pathList；
        Field pathListField = findField(pathClassLoader, "pathList");

        // 3. 反射修改pathList的dexElemnts，即重新赋值
        try{
            Object pathList = pathListField.get(pathClassLoader);
            // 3.1 把补丁包patch.dex转化为Elment[],即patchElements
            // 通过调用pathList对象的makePathElement方法实现，该方法需要存储三个参数
            //    files: 补丁dex文件列表
            //    optimizedDirectory: 存储dex优化后的odex文件目录，我们可以自定义
            //    suppressedExceptions: new一个即可
            Method makePathElementsMthod = findMethod(pathList, "makePathElements", List.class,File.class, List.class);
            Object[] patchElements = (Object[]) makePathElementsMthod.invoke(pathList, patches, mOdexDirectory, suppressedExceptions);

            // 3.2 获得pathList的dexElements属性，类型为Elment[]
            // 即 oldElements
            Field dexElementsField = findField(pathList, "dexElements");
            Object[] oldElements = (Object[]) dexElementsField.get(pathList);

            // 3.3 将path Elment[]和old Elment[]合并，并反射赋值给pathList的dexElements；
            Object[] newElements = (Object[]) Array.newInstance(oldElements.getClass().getComponentType(), patchElements.length + oldElements.length);
            System.arraycopy(patchElements, 0, newElements, 0, patchElements.length);
            System.arraycopy(oldElements, 0, newElements, patchElements.length, oldElements.length);
            dexElementsField.set(pathList, newElements);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "修复完成");
    }

    private static Field findField(Object instance, String fieldName) {
        // 获取instance的Class对象
        Class<?> clz = instance.getClass();
        // 得到Class对象的field属性
        // 遵循双亲委托机制
        while(clz != null) {
            try {
                Field filed = clz.getDeclaredField(fieldName);
                if(filed != null) {
                    // 设置访问权限
                    filed.setAccessible(true);
                    return filed;
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            clz = clz.getSuperclass();
        }
        throw new IllegalArgumentException("instance:"+ instance.getClass().getSimpleName()+"do not find "+ fieldName);
    }

    private static Method findMethod(Object instance, String methodName, Class<?>... paramters) {
        Class<?> clz = instance.getClass();

        while (clz != null) {
            try {
                Method method = clz.getDeclaredMethod(methodName, paramters);
                if(method != null) {
                    method.setAccessible(true);
                    return method;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            clz = clz.getSuperclass();
        }

        throw new IllegalArgumentException("instance:"+ instance.getClass().getSimpleName()+"do not find "+ methodName);
    }
}
