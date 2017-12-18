package com.liuh.mtoutiao.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liuh.mtoutiao.R;
import com.liuh.mtoutiao.service.entity.NewsDetail;
import com.liuh.mtoutiao.service.presenter.BasePresenter;
import com.liuh.mtoutiao.ui.base.BaseActivity;

public class VideoDetailActivity extends NewsDetailBaseActivity {


    @Override
    public void onGetNewsDetailSuccess(NewsDetail newsDetail) {

    }

    @Override
    protected int getViewContentViewId() {
        return 0;
    }
}
