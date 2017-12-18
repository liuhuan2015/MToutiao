package com.liuh.mtoutiao.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.liuh.mtoutiao.ui.activity.NewsDetailBaseActivity;
import com.liuh.mtoutiao.ui.activity.NewsDetailDetailActivity;
import com.liuh.mtoutiao.ui.activity.VideoDetailActivity;
import com.liuh.mtoutiao.ui.activity.WebViewActivity;
import com.liuh.mtoutiao.ui.adapter.NewsApdater;
import com.liuh.mtoutiao.ui.base.BaseFragment;
import com.liuh.mtoutiao.ui.iview.BookView;
import com.liuh.mtoutiao.ui.iview.INewsListView;
import com.liuh.mtoutiao.ui.utils.ListUtils;
import com.liuh.mtoutiao.ui.utils.LogUtil;
import com.liuh.mtoutiao.ui.utils.NetWorkUtils;
import com.liuh.mtoutiao.ui.utils.NewsRecordHelper;
import com.liuh.mtoutiao.ui.utils.UIUtils;
import com.liuh.uikit.TipView;
import com.liuh.uikit.powerfulrecyclerview.PowerfulRecyclerView;
import com.liuh.uikit.refreshlayout.BGANormalRefreshViewHolder;
import com.liuh.uikit.refreshlayout.BGARefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

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

        mNewsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                News news = mNewsList.get(position);

                String itemId = news.item_id;
                StringBuffer urlSb = new StringBuffer("http://m.toutiao.com/i");
                urlSb.append(itemId).append("/info/");
                String url = urlSb.toString();//http://m.toutiao.com/i6412427713050575361/info/
                Intent intent = null;
                if (news.has_video) {
                    //视频
                    intent = new Intent(mActivity, VideoDetailActivity.class);
                    if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                        //传递进度
                        int progress = JCMediaManager.instance().mediaPlayer.getCurrentPosition();
                        if (progress != 0) {
                            intent.putExtra(VideoDetailActivity.PROGRESS, progress);
                        }
                    }
                } else {
                    //非视频新闻
                    if (news.article_type == 1) {
                        //如果article_type为1,则是使用WebViewActivity打开
                        intent = new Intent(mActivity, WebViewActivity.class);
                        intent.putExtra(WebViewActivity.URL, news.article_type);
                        startActivity(intent);
                        return;
                    }
                    intent = new Intent(mActivity, NewsDetailDetailActivity.class);
                }
                intent.putExtra(NewsDetailBaseActivity.CHANNEL_CODE, mChannelCode);
                intent.putExtra(NewsDetailBaseActivity.POSITION, position);
                intent.putExtra(NewsDetailBaseActivity.DETAIL_URL, url);
                intent.putExtra(NewsDetailBaseActivity.GROUP_ID, news.group_id);
                intent.putExtra(NewsDetailBaseActivity.ITEM_ID, itemId);

                startActivity(intent);
            }
        });

        if (isVideoList) {
            //如果是视频列表,监听滑动
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (JCVideoPlayerManager.getCurrentJcvd() != null) {
                        JCVideoPlayerStandard videoPlayerStandard = (JCVideoPlayerStandard) JCVideoPlayerManager.getCurrentJcvd();
                        if (videoPlayerStandard.currentState == JCVideoPlayerStandard.CURRENT_STATE_PLAYING) {
                            //如果正在播放
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                            int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                            if (firstVisibleItemPosition > videoPlayerStandard.getPosition() || lastVisibleItemPosition < videoPlayerStandard.getPosition()) {
                                //如果第一个可见的条目位置大于当前播放videoplayer的位置
                                //或者最后一个条目的位置小于当前播放videoplayer的位置,释放资源
                                JCVideoPlayerStandard.releaseAllVideos();
                            }
                        }
                    }
                }
            });
        }
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
        if (!NetWorkUtils.isNetworkAvailable(mActivity)) {
            //网络不可用,弹出提示
            mTipView.show();
            if (mRefreshLayout.getCurrentRefreshStatus() == BGARefreshLayout.RefreshStatus.REFRESHING) {
                mRefreshLayout.endRefreshing();
            }
            return;
        }
        mPresenter.getNewsList(mChannelCode);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        //BGARefreshLayout的加载更多,不处理,使用的是BaseQuickAdapter的加载更多
        return false;
    }

    @Override
    public void onLoadMoreRequested() {
        //BaseQuickAdapter的加载更多,没有请求网络数据,而是从本地数据库中取数据
        if (mNewsRecord.getPage() == 0 || mNewsRecord.getPage() == 1) {
            //如果记录的页数为0(即是创建的空记录),或者页数为1(即已经是第一条记录了)
            mNewsAdapter.loadMoreEnd();
            return;
        }

        NewsRecord preNewsRecord = NewsRecordHelper.getPreNewsRecord(mChannelCode, mNewsRecord.getPage());
        if (preNewsRecord == null) {
            mNewsAdapter.loadMoreEnd();
            return;
        }

        mNewsRecord = preNewsRecord;

        long startTime = System.currentTimeMillis();

        List<News> newsList = NewsRecordHelper.convertToNewsLits(mNewsRecord.getJson());

        if (isRecommendChannel) {
            //如果是推荐频道
            newsList.remove(0);//移除第一个,因为第一个是置顶新闻,重复
        }

        long endTime = System.currentTimeMillis();
        //由于是读取数据库,如果耗时不足1秒,则1秒后才收起加载更多
        if (endTime - startTime <= 1000) {
            UIUtils.postTaskDelay(new Runnable() {
                @Override
                public void run() {
                    mNewsAdapter.loadMoreComplete();
                    mNewsList.addAll(newsList);//添加到集合下面
                    mNewsAdapter.notifyDataSetChanged();//刷新adapter

                }
            }, (int) (1000 - (endTime - startTime)));
        }

    }

    @Override
    public void onGetNewsListSuccess(List<News> newsList, String tipInfo) {
        LogUtil.e("onGetNewsListSuccess", "---------tipInfo:" + tipInfo);
        mRefreshLayout.endRefreshing();//加载完毕后在UI线程结束下拉刷新

        if (ListUtils.isEmpty(mNewsList)) {
            //本地没有存放过数据，说明是第一次请求网络
            if (ListUtils.isEmpty(newsList)) {
                mStateView.showEmpty();
                return;
            }
            mStateView.showContent();
        }
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
        mTipView.show();
        if (ListUtils.isEmpty(mNewsList)) {
            //显示重试的布局
            mStateView.showEmpty();
        }

        //收起刷新
        if (mRefreshLayout.getCurrentRefreshStatus() == BGARefreshLayout.RefreshStatus.REFRESHING) {
            mRefreshLayout.endRefreshing();
        }

    }
}
