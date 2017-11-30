package com.liuh.mtoutiao.app.base;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * Author:liuh
 * Date: 2017/11/29 17:54
 * Description:
 */

public class BaseApp extends MultiDexApplication {

    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    private static Context mContext;//上下文
    private static Thread mMainThread;//主线程
    private static long mMainThreadId;//主线程id
    private static Looper mMainLooper;//主线程的消息循环队列
    private static Handler mHandler;//主线程的Handler

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //对全局属性赋值
        mContext = getApplicationContext();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        mHandler = new Handler();

        //******************************相关第三方SDK的初始化等操作******************************

        LitePal.initialize(this);//初始化LitePal
    }

    /**
     * 重启当前应用
     */
    public static void restart() {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    public static Context getmContext() {
        return mContext;
    }

    public static void setmContext(Context mContext) {
        BaseApp.mContext = mContext;
    }

    public static Thread getmMainThread() {
        return mMainThread;
    }

    public static void setmMainThread(Thread mMainThread) {
        BaseApp.mMainThread = mMainThread;
    }

    public static long getmMainThreadId() {
        return mMainThreadId;
    }

    public static void setmMainThreadId(long mMainThreadId) {
        BaseApp.mMainThreadId = mMainThreadId;
    }

    public static Looper getmMainLooper() {
        return mMainLooper;
    }

    public static void setmMainLooper(Looper mMainLooper) {
        BaseApp.mMainLooper = mMainLooper;
    }

    public static Handler getmHandler() {
        return mHandler;
    }

    public static void setmHandler(Handler mHandler) {
        BaseApp.mHandler = mHandler;
    }
}
