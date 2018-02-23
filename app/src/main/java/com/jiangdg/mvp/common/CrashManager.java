package com.jiangdg.mvp.common;

import android.os.Environment;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/** 异常捕获处理
 *  将异常信息保存到本地，并上传到服务器
 * Created by jianddongguo on 2018/2/23.
 */

public class CrashManager implements Thread.UncaughtExceptionHandler {
    private static final String LOGPATH = Environment.getExternalStorageDirectory()+
            "/mvp/logs/crash.txt";
    private CrashManager mCrashHandler;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashManager() {}

    public CrashManager getInstance() {
        if(mCrashHandler == null) {
            mCrashHandler = new CrashManager();
        }
        return mCrashHandler;
    }

    public void initCrashHandler() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(mDefaultHandler);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // handleException，自定义处理异常
        // 否则，让系统默认异常处理器来处理
        if(! handleException(e) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(t, e);
        }
    }

    private boolean handleException(Throwable e) {
        if(e == null) {
            return false;
        }
        // 将异常信息保存到本地
        saveErrorInfo(e);
        // 将异常信息上传到服务器
        uploadExcepInfo();
        return true;
    }

    private void saveErrorInfo(Throwable e) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause.getCause();
        }
        printWriter.close();
        // 收集各种信息
        String errorInfo = writer.toString();

        File file = new File(LOGPATH);
    }

    private void uploadExcepInfo() {

    }
}
