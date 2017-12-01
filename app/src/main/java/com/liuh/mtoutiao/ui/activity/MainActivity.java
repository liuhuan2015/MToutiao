package com.liuh.mtoutiao.ui.activity;

import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;
import com.liuh.mtoutiao.R;
import com.liuh.mtoutiao.service.presenter.BasePresenter;
import com.liuh.mtoutiao.ui.adapter.MainTabAdapter;
import com.liuh.mtoutiao.ui.base.BaseActivity;
import com.liuh.mtoutiao.ui.base.BaseFragment;
import com.liuh.mtoutiao.ui.fragment.HomeFragment;
import com.liuh.mtoutiao.ui.fragment.MeFragment;
import com.liuh.mtoutiao.ui.fragment.MicroFragment;
import com.liuh.mtoutiao.ui.fragment.VideoFragment;
import com.liuh.mtoutiao.ui.widget.NoScrollViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseActivity {
    @BindView(R.id.vp_content)
    NoScrollViewPager mVpContent;
    @BindView(R.id.bottom_bar)
    BottomBarLayout mBottomBarLayout;

    private List<BaseFragment> mFragments;

    private MainTabAdapter mTabAdapter;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        mFragments = new ArrayList<BaseFragment>(4);
        mFragments.add(new HomeFragment());
        mFragments.add(new VideoFragment());
        mFragments.add(new MicroFragment());
        mFragments.add(new MeFragment());
    }

    @Override
    protected void initListener() {
        mTabAdapter = new MainTabAdapter(getSupportFragmentManager(), mFragments);
        mVpContent.setAdapter(mTabAdapter);
        //设置预加载页面为4，即打开应用，就直接全部加载。
        mVpContent.setOffscreenPageLimit(mFragments.size());
        mBottomBarLayout.setViewPager(mVpContent);

        mBottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int position) {

            }
        });


    }
}
