package com.liuh.mtoutiao.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.liuh.mtoutiao.app.listener.PermissionListener;
import com.liuh.mtoutiao.service.presenter.BasePresenter;
import com.liuh.mtoutiao.ui.activity.MainActivity;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Author:liuh
 * Date: 2017/11/29:16:52
 * Description:
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {
    protected T mPresenter;
    private static long mPreTime;
    private static Activity mCurrentActivity;//对所有的Activity进行管理
    public static List<Activity> mActivities = new LinkedList<Activity>();
    protected Bundle saveInstanceState;
    public PermissionListener mPermissionListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        this.saveInstanceState = savedInstanceState;
        //初始化的时候将其添加到集合中
        synchronized (mActivities) {
            mActivities.add(this);
        }

        mPresenter = createPresenter();

        setContentView(provideContentViewId());
        ButterKnife.bind(this);

        initView();

        initData();

        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCurrentActivity = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        synchronized (mActivities) {
            mActivities.remove(this);
        }

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void onBackPressed() {
        if (mCurrentActivity instanceof MainActivity) {
            if (System.currentTimeMillis() - mPreTime > 2000) {


            }

        }


        super.onBackPressed();
    }

    private void initView() {

    }


    private void initData() {

    }

    private void initListener() {

    }


    //用于创建Presenter,由子类实现
    protected abstract T createPresenter();

    //提供当前界面的布局文件id，由子类实现
    protected abstract int provideContentViewId();


}
