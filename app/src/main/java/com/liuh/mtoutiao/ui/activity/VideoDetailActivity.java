package com.liuh.mtoutiao.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liuh.mtoutiao.R;
import com.liuh.mtoutiao.service.presenter.BasePresenter;
import com.liuh.mtoutiao.ui.base.BaseActivity;

public class VideoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return 0;
    }
}
