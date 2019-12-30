package com.jiangdg.hotfix.hotfix.androidN;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

public class NewClassLoaderInjector {


    private static final class DispatchClassLoader extends ClassLoader {
        private final String mApplicationClassName;
        private final ClassLoader mOldClassLoader;

        private ClassLoader mNewClassLoader;

        private final ThreadLocal<Boolean> mCallFindClassOfLeafDirectly = new ThreadLocal<Boolean>() {
            @Override
            protected Boolean initialValue() {
                return false;
            }
        };

        DispatchClassLoader(String applicationClassName, ClassLoader oldClassLoader) {
            super(ClassLoader.getSystemClassLoader());
            mApplicationClassName = applicationClassName;
            mOldClassLoader = oldClassLoader;
        }

        void setNewClassLoader(ClassLoader classLoader) {
            mNewClassLoader = classLoader;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if (mCallFindClassOfLeafDirectly.get()) {
                return null;
            }
            // 1、Application类不需要修复，使用原本的类加载器获得
            if (name.equals(mApplicationClassName)) {
                return findClass(mOldClassLoader, name);
            }
            // 2、加载热修复框架的类 因为不需要修复，就用原本的类加载器获得
            if (name.startsWith("com.enjoy.patch.")) {
                return findClass(mOldClassLoader, name);
            }

            /**
             * Android P中添加了新特性，内联类的调用者必须和被调用者是相同的classloader
             * 而有些程序可能开启了：useLibrary 'org.apache.http.legacy'
             * 使用到了apache的包装类，
             * 系统就会插入一个名为“org.apache.http.legacy.boot.jar”的jar到classloader中
             * 如果我们使用自定义的classloader代替系统的classloader，
             * 可能会导致 apache的内部类被我们的类加载器加载，而调用这些内联类的类被 老的类加载器加载
             * 从而出现崩溃。
             * 所以让apache的类全部被系统原本的类加载器加载，反正也不用修复它们。
             */
            if (name.startsWith("org.apache.commons.codec.")
                    || name.startsWith("org.apache.commons.logging.")
                    || name.startsWith("org.apache.http.")) {
                return findClass(mOldClassLoader, name);
            }

            try {
                return findClass(mNewClassLoader, name);
            } catch (ClassNotFoundException ignored) {
                return findClass(mOldClassLoader, name);
            }
        }

        private Class<?> findClass(ClassLoader classLoader, String name) throws ClassNotFoundException {
            try {
                //双亲委托，所以可能会stackoverflow死循环，防止这个情况
                mCallFindClassOfLeafDirectly.set(true);
                return classLoader.loadClass(name);
            } finally {
                mCallFindClassOfLeafDirectly.set(false);
            }
        }
    }

    public static ClassLoader inject(Application app, ClassLoader oldClassLoader) throws Throwable {
        // 分发加载任务的加载器，作为我们自己的加载器的父加载器
        DispatchClassLoader dispatchClassLoader
                = new DispatchClassLoader(app.getClass().getName(), oldClassLoader);
        //创建我们自己的加载器
        ClassLoader newClassLoader
                = createNewClassLoader(app, oldClassLoader, dispatchClassLoader);

        dispatchClassLoader.setNewClassLoader(newClassLoader);

        doInject(app, newClassLoader);
        return newClassLoader;
    }

    private static ClassLoader createNewClassLoader(Context context, ClassLoader oldClassLoader,
                                                    ClassLoader dispatchClassLoader) throws Throwable {
        //得到pathList
        Field pathListField = ShareReflectUtil.findField(oldClassLoader, "pathList");
        Object oldPathList = pathListField.get(oldClassLoader);

        //dexElements
        Field dexElementsField = ShareReflectUtil.findField(oldPathList, "dexElements");
        Object[] oldDexElements = (Object[]) dexElementsField.get(oldPathList);

        //从Element上得到 dexFile
        Field dexFileField = ShareReflectUtil.findField(oldDexElements[0], "dexFile");

        // 获得原始的dexPath用于构造classloader
        StringBuilder dexPathBuilder = new StringBuilder();
        String packageName = context.getPackageName();
        boolean isFirstItem = true;
        for (Object oldDexElement : oldDexElements) {
            String dexPath = null;
            DexFile dexFile = (DexFile) dexFileField.get(oldDexElement);
            if (dexFile != null) {
                dexPath = dexFile.getName();
            }
            if (dexPath == null || dexPath.isEmpty()) {
                continue;
            }
            if (!dexPath.contains("/" + packageName)) {
                continue;
            }
            if (isFirstItem) {
                isFirstItem = false;
            } else {
                dexPathBuilder.append(File.pathSeparator);
            }
            dexPathBuilder.append(dexPath);
        }

        final String combinedDexPath = dexPathBuilder.toString();

        //  app的native库（so） 文件目录 用于构造classloader
        Field nativeLibraryDirectoriesField = ShareReflectUtil.findField(oldPathList, "nativeLibraryDirectories");
        List<File> oldNativeLibraryDirectories = (List<File>) nativeLibraryDirectoriesField.get(oldPathList);


        StringBuilder libraryPathBuilder = new StringBuilder();
        isFirstItem = true;
        for (File libDir : oldNativeLibraryDirectories) {
            if (libDir == null) {
                continue;
            }
            if (isFirstItem) {
                isFirstItem = false;
            } else {
                libraryPathBuilder.append(File.pathSeparator);
            }
            libraryPathBuilder.append(libDir.getAbsolutePath());
        }

        String combinedLibraryPath = libraryPathBuilder.toString();

        //创建自己的类加载器
        ClassLoader result = new PathClassLoader(combinedDexPath, combinedLibraryPath, dispatchClassLoader);

        ShareReflectUtil.findField(oldPathList, "definingContext").set(oldPathList, result);
        ShareReflectUtil.findField(result, "parent").set(result, dispatchClassLoader);
        return result;
    }


    private static void doInject(Application app, ClassLoader classLoader) throws Throwable {
        Thread.currentThread().setContextClassLoader(classLoader);

        Context baseContext = (Context) ShareReflectUtil.findField(app, "mBase").get(app);
        Object basePackageInfo = ShareReflectUtil.findField(baseContext, "mPackageInfo").get(baseContext);
        ShareReflectUtil.findField(basePackageInfo, "mClassLoader").set(basePackageInfo, classLoader);

        if (Build.VERSION.SDK_INT < 27) {
            Resources res = app.getResources();
            try {
                ShareReflectUtil.findField(res, "mClassLoader").set(res, classLoader);

                final Object drawableInflater = ShareReflectUtil.findField(res, "mDrawableInflater").get(res);
                if (drawableInflater != null) {
                    ShareReflectUtil.findField(drawableInflater, "mClassLoader").set(drawableInflater, classLoader);
                }
            } catch (Throwable ignored) {
                // Ignored.
            }
        }
    }
}
