package com.liuh.mtoutiao.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.liuh.mtoutiao.R;
import com.liuh.mtoutiao.app.constants.Constant;
import com.liuh.mtoutiao.service.entity.Book;
import com.liuh.mtoutiao.service.entity.News;
import com.liuh.mtoutiao.service.entity.NewsRecord;
import com.liuh.mtoutiao.service.presenter.BookPresenter;
import com.liuh.mtoutiao.service.presenter.NewsListPresenter;
import com.liuh.mtoutiao.ui.adapter.NewsApdater;
import com.liuh.mtoutiao.ui.base.BaseFragment;
import com.liuh.mtoutiao.ui.iview.BookView;
import com.liuh.mtoutiao.ui.iview.INewsListView;
import com.liuh.mtoutiao.ui.utils.ListUtils;
import com.liuh.mtoutiao.ui.utils.LogUtil;
import com.liuh.mtoutiao.ui.utils.NewsRecordHelper;
import com.liuh.mtoutiao.ui.utils.UIUtils;
import com.liuh.uikit.TipView;
import com.liuh.uikit.powerfulrecyclerview.PowerfulRecyclerView;
import com.liuh.uikit.refreshlayout.BGANormalRefreshViewHolder;
import com.liuh.uikit.refreshlayout.BGARefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author:liuh
 * Date: 2017/12/1 13:54
 * Description:展示每个频道新闻列表的fragment
 */

public class NewsListFragment extends BaseFragment<NewsListPresenter> implements INewsListView, BGARefreshLayout.BGARefreshLayoutDelegate,
        BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.tip_view)
    TipView mTipView;

    @BindView(R.id.refresh_layout)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.fl_content)
    FrameLayout mFrameLayout;

    @BindView(R.id.rv_news)
    PowerfulRecyclerView mRecyclerView;

    @BindView(R.id.tv)
    TextView textView;

    private String mChannelCode;
    //是否是是视频列表
    private boolean isVideoList;

    /**
     * 是否是推荐频道
     */
    private boolean isRecommendChannel;
    private List<News> mNewsList = new ArrayList<>();
    protected BaseQuickAdapter mNewsAdapter;

    /**
     * 是否是点击底部标签进行刷新的标识
     */
    private boolean isClickTabRefreshing;
    private RotateAnimation mRotateAnimation;
    private Gson mGson = new Gson();
    //新闻记录
    private NewsRecord mNewsRecord;

    //用于标记是否是首页的底部刷新，如果是，加载成功后发送完成的事件
    private boolean isHomeTabRefresh;

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_newslist;
    }

    @Override
    protected NewsListPresenter createPresenter() {
        return new NewsListPresenter(this);
    }

    @Override
    protected void initView(View rootView) {
        mRefreshLayout.setDelegate(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 1));
        //设置下拉刷新和上拉加载的风格
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(mActivity, false);
        //设置下拉刷新
        refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.pull_refresh_bg);
        refreshViewHolder.setPullDownRefreshText(UIUtils.getString(R.string.refresh_pull_down_text));
        refreshViewHolder.setReleaseRefreshText(UIUtils.getString(R.string.refresh_release_text));
        refreshViewHolder.setRefreshingText(UIUtils.getString(R.string.refresh_ing_text));

        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        mRefreshLayout.shouldHandleRecyclerViewLoadingMore(mRecyclerView);
    }

    @Override
    protected void initData() {
        mChannelCode = getArguments().getString(Constant.CHANNEL_CODE);
        isVideoList = getArguments().getBoolean(Constant.IS_VIDEO_LIST, false);

        String[] channelCodes = UIUtils.getStringArr(R.array.channel_code);
        isRecommendChannel = mChannelCode.equals(channelCodes[0]);//是否是推荐频道
    }

    @Override
    protected void initListener() {
        mNewsAdapter = new NewsApdater(mActivity, mChannelCode, isVideoList, mNewsList);
        mRecyclerView.setAdapter(mNewsAdapter);

        mNewsAdapter.setEnableLoadMore(true);
        mNewsAdapter.setOnLoadMoreListener(this, mRecyclerView);

    }

    @Override
    protected void loadData() {
        mStateView.showLoading();
        //查找该频道的最后一组记录
        mNewsRecord = NewsRecordHelper.getLastNewsRecord(mChannelCode);
        if (mNewsRecord == null) {
            //找不到记录，拉取网络数据
            mNewsRecord = new NewsRecord();
            LogUtil.e("NewsListFragment", "..............mChannelCode:" + mChannelCode);
            mPresenter.getNewsList(mChannelCode);
            return;
        }

        //找到最后一组记录，转换成新闻集合并展示
        List<News> newsList = NewsRecordHelper.convertToNewsLits(mNewsRecord.getJson());
        mNewsList.addAll(newsList);
        mNewsAdapter.notifyDataSetChanged();
        mStateView.showContent();

        //如果时间超过10分钟，则自动刷新
        if (mNewsRecord.getTime() - System.currentTimeMillis() >= 10 * 60 * 1000) {
            mRefreshLayout.beginRefreshing();
        }

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void onGetNewsListSuccess(List<News> newsList, String tipInfo) {
        LogUtil.e("onGetNewsListSuccess", "---------tipInfo:" + tipInfo);
        mRefreshLayout.endRefreshing();//加载完毕后在UI线程结束下拉刷新

        if (ListUtils.isEmpty(mNewsList)) {
            //本地没有存放过数据，说明是第一次请求网络
            if (ListUtils.isEmpty(newsList)) {
                mStateView.showEmpty();
            } else
                mStateView.showContent();
        } else {
            //本地存放有数据
            if (ListUtils.isEmpty(newsList)) {
                UIUtils.showToast("目前没有最新新闻了");
            } else {
                if (TextUtils.isEmpty(newsList.get(0).title)) {
                    //由于汽车、体育等频道第一条属于导航的内容，所以如果第一条没有标题，则移除
                    newsList.remove(0);
                }
                dealRepeat(newsList);//处理新闻重复问题
                mNewsList.addAll(0, newsList);
                mNewsAdapter.notifyDataSetChanged();
                mTipView.show(tipInfo);
                //保存到数据库
                NewsRecordHelper.save(mChannelCode, mGson.toJson(newsList));
            }
        }

    }

    /**
     * 处理置顶新闻和广告重复
     *
     * @param newsList
     */
    private void dealRepeat(List<News> newsList) {
        if (isRecommendChannel && !ListUtils.isEmpty(mNewsList)) {
            //如果是推荐频道并且数据列表已经有数据，处理置顶新闻或广告重复的问题
            mNewsList.remove(0);//由于第一条新闻是重复的，移除原有的第一条
            //新闻列表通常第4个是广告,除了第一次有广告,再次获取的都移除广告
            if (newsList.size() >= 4) {
                News fourthNews = newsList.get(3);
                //如果列表第四个tag是广告,则移除
                if (fourthNews.tag.equals(Constant.ARTICLE_GENRE_AD)) {
                    newsList.remove(fourthNews);
                }
            }
        }
    }

    @Override
    public void onError() {

    }
}
