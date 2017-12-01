package com.liuh.mtoutiao.ui.fragment;


import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liuh.mtoutiao.R;
import com.liuh.mtoutiao.app.constants.Constant;
import com.liuh.mtoutiao.service.entity.Channel;
import com.liuh.mtoutiao.service.presenter.BasePresenter;
import com.liuh.mtoutiao.ui.base.BaseFragment;
import com.liuh.mtoutiao.ui.utils.PreUtils;
import com.liuh.mtoutiao.ui.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.weyye.library.colortrackview.ColorTrackTabLayout;

/**
 * Author:liuh
 * Date: 2017/11/30 15:29
 * Description:
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.tv_search)
    TextView mTvSearch;

    @BindView(R.id.tab_channel)
    ColorTrackTabLayout mTabChannel;

    @BindView(R.id.iv_addchannel)
    ImageView mIvAddChannel;

    @BindView(R.id.vp_homecontent)
    ViewPager mVpContent;

    private List<Channel> mSelectedChannels = new ArrayList<>();
    private List<Channel> mUnSelectedChannels = new ArrayList<>();

    private Gson mGson = new Gson();


    @Override
    protected void initData() {
        initChannelData();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    /**
     * 初始化已选频道和未选频道的数据
     * 假数据，此处数据本应该是通过网络获取的
     */
    private void initChannelData() {
        String selectedChannelJson = PreUtils.getString(Constant.SELECTED_CHANNEL_JSON, "");
        String unselectedChannelJson = PreUtils.getString(Constant.UNSELECTED_CHANNEL_JSON, "");

        if (TextUtils.isEmpty(selectedChannelJson)) {
            //本地没有title存储
            String[] channelNames = getResources().getStringArray(R.array.channel);
            String[] channelCodes = getResources().getStringArray(R.array.channel_code);
            //默认添加全部频道
            for (int i = 0; i < channelNames.length; i++) {
                String title = channelNames[i];
                String code = channelCodes[i];
                mSelectedChannels.add(new Channel(title, code));
            }
            selectedChannelJson = mGson.toJson(mSelectedChannels);
            PreUtils.putString(Constant.SELECTED_CHANNEL_JSON, selectedChannelJson);
        } else {
            //本地有title存储
//            List<Channel> selectedChannel=mGson.fromJson(selectedChannelJson,List<Channel>);


        }


    }


    @OnClick(R.id.tv_search)
    public void onSearchTvClicked(View view) {
        UIUtils.showToast("跳转到搜索界面");
    }


}
