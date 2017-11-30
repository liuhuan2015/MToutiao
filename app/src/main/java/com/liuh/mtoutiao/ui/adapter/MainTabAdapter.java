package com.liuh.mtoutiao.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.liuh.mtoutiao.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:liuh
 * Date: 2017/11/30 15:43
 * Description:
 */

public class MainTabAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();


    public MainTabAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        if (fragments != null) {
            this.fragments = fragments;
        }

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
