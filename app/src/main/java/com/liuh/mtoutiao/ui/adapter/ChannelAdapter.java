package com.liuh.mtoutiao.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.liuh.mtoutiao.service.entity.Channel;
import com.liuh.mtoutiao.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:liuh
 * Date: 2017/12/1 13:41
 * Description:
 */

public class ChannelAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();
    private List<Channel> channels = new ArrayList<Channel>();

    public ChannelAdapter(FragmentManager fm, List<BaseFragment> fragments, List<Channel> channels) {
        super(fm);
        this.fragments = fragments;
        this.channels = channels;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return channels.get(position).title;
    }
}
