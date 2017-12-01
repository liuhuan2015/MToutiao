package com.liuh.mtoutiao.ui.fragment;

import com.liuh.mtoutiao.R;
import com.liuh.mtoutiao.service.presenter.BasePresenter;
import com.liuh.mtoutiao.ui.base.BaseFragment;

/**
 * Author:liuh
 * Date: 2017/12/1 13:54
 * Description:
 */

public class NewsListFragment extends BaseFragment {

    public NewsListFragment() {
    }



    @Override
    protected void loadData() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_newslist;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
