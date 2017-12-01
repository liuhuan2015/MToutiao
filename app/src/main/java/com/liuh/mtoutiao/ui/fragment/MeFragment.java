package com.liuh.mtoutiao.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuh.mtoutiao.R;
import com.liuh.mtoutiao.service.presenter.BasePresenter;
import com.liuh.mtoutiao.ui.base.BaseFragment;
import com.liuh.mtoutiao.ui.utils.LogUtil;

/**
 * Author:liuh
 * Date: 2017/11/30 15:31
 * Description:
 */

public class MeFragment extends BaseFragment {

    @Override
    protected void loadData() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_me;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("MeFragment", "--------------------MeFragment---------------create");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LogUtil.e("HomeFragment", "--------------------MeFragment---------------onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("MeFragment", "--------------------MeFragment---------------onDestroy");
    }
}
