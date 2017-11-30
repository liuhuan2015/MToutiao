package com.liuh.mtoutiao.ui.fragment;

import com.liuh.mtoutiao.R;
import com.liuh.mtoutiao.service.presenter.BasePresenter;
import com.liuh.mtoutiao.ui.base.BaseFragment;

/**
 * Author:liuh
 * Date: 2017/11/30 15:30
 * Description:
 */

public class MicroFragment extends BaseFragment {

    @Override
    protected void loadData() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_micro;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
