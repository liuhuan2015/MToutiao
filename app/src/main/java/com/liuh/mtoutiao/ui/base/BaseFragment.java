package com.liuh.mtoutiao.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.nukc.stateview.StateView;
import com.liuh.mtoutiao.R;
import com.liuh.mtoutiao.service.presenter.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Author:liuh
 * Date: 2017/11/30 15:28
 * Description:
 */

public abstract class BaseFragment<T extends BasePresenter> extends LazyLoadFragment {
    protected T mPresenter;

    protected Activity mActivity;

    protected StateView mStateView;//用于显示加载中，网络异常，空布局，内容布局

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(provideContentViewId(), container, false);
            ButterKnife.bind(this, rootView);

            mStateView = StateView.inject(getStateViewRoot());
            if (mStateView != null) {
                mStateView.setLoadingResource(R.layout.page_loading);
                mStateView.setRetryResource(R.layout.page_net_error);
            }

            initView(rootView);
            initData();
            initListener();

        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    /**
     * StateView的根布局，默认是整个界面，如果需要变换可以重写此方法
     */
    public View getStateViewRoot() {
        return rootView;
    }

    @Override
    protected void onFragmentFirstVisible() {
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        rootView = null;
    }

    protected abstract void loadData();

    protected void initView(View rootView) {

    }

    protected void initData() {

    }

    protected void initListener() {

    }

    protected abstract int provideContentViewId();


    protected abstract T createPresenter();

    public boolean isEventBusRegisted(Object subscribe) {
        return EventBus.getDefault().isRegistered(subscribe);
    }

    public void registerEventBus(Object subscribe) {
        if (!isEventBusRegisted(subscribe)) {
            EventBus.getDefault().register(subscribe);
        }
    }

    public void unregisterEventBus(Object subscribe) {
        if (isEventBusRegisted(subscribe)) {
            EventBus.getDefault().unregister(subscribe);
        }
    }
}
