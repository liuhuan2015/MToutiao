package com.liuh.mtoutiao.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.nukc.stateview.StateView;
import com.liuh.mtoutiao.R;
import com.liuh.mtoutiao.app.constants.Constant;
import com.liuh.mtoutiao.service.entity.CommentData;
import com.liuh.mtoutiao.service.presenter.NewsDetailPresenter;
import com.liuh.mtoutiao.service.response.CommentResponse;
import com.liuh.mtoutiao.ui.adapter.CommentAdapter;
import com.liuh.mtoutiao.ui.base.BaseActivity;
import com.liuh.mtoutiao.ui.iview.INewsDetailView;
import com.liuh.mtoutiao.ui.utils.ListUtils;
import com.liuh.mtoutiao.ui.widget.NewsDetailHeaderView;
import com.liuh.uikit.powerfulrecyclerview.PowerfulRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Date: 2017/12/15 08:47
 * Description:新闻详情页的基类
 */

public abstract class NewsDetailBaseActivity extends BaseActivity<NewsDetailPresenter> implements INewsDetailView, BaseQuickAdapter.RequestLoadMoreListener {

    public static final String CHANNEL_CODE = "channelcode";
    public static final String PROGRESS = "progress";
    public static final String POSITION = "position";
    public static final String DETAIL_URL = "detailurl";
    public static final String GROUP_ID = "groupid";
    public static final String ITEM_ID = "itemid";

    @BindView(R.id.fl_content)
    FrameLayout mFlContent;

    @BindView(R.id.rv_comment)
    PowerfulRecyclerView mRvComment;

    @BindView(R.id.tv_comment_count)
    TextView mTvCommentCount;

    private List<CommentData> mCommentList = new ArrayList<>();
    protected StateView mStateView;
    private CommentAdapter mCommentAdapter;
    protected NewsDetailHeaderView mHeaderView;
    private String mDetailUrl;
    private String mGroupId;
    private String mItemId;
    protected CommentResponse mCommentResponse;

    protected String mChannelCode;
    protected int mPosition;


    @Override
    protected NewsDetailPresenter createPresenter() {
        return new NewsDetailPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return getViewContentViewId();
    }

    protected abstract int getViewContentViewId();

    @Override
    protected void initView() {
        mStateView = StateView.inject(mFlContent);
        mStateView.setLoadingResource(R.layout.page_loading);
        mStateView.setRetryResource(R.layout.page_net_error);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mChannelCode = intent.getStringExtra(CHANNEL_CODE);
        mPosition = intent.getIntExtra(POSITION, 0);

        mDetailUrl = intent.getStringExtra(DETAIL_URL);
        mGroupId = intent.getStringExtra(GROUP_ID);
        mItemId = intent.getStringExtra(ITEM_ID);
        mItemId = mItemId.replace("i", "");

        mPresenter.getNewsDetail(mDetailUrl);

        loadCommentData();
    }

    private void loadCommentData() {
        mStateView.showLoading();
        mPresenter.getComment(mGroupId, mItemId, 1);
    }

    @Override
    protected void initListener() {
        mCommentAdapter = new CommentAdapter(this, R.layout.item_comment, mCommentList);
        mRvComment.setAdapter(mCommentAdapter);

        mHeaderView = new NewsDetailHeaderView(this);
        mCommentAdapter.addHeaderView(mHeaderView);

        mCommentAdapter.setEnableLoadMore(true);
        mCommentAdapter.setOnLoadMoreListener(this, mRvComment);

        mCommentAdapter.setEmptyView(R.layout.pager_no_comment);
        mCommentAdapter.setHeaderAndEmpty(true);

        mStateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {

            }
        });


    }

    @Override
    public void onGetCommentSuccess(CommentResponse response) {
        mCommentResponse = response;
        if (ListUtils.isEmpty(mCommentResponse.data)) {
            //没有评论了
            mCommentAdapter.loadMoreEnd();
            return;
        }

        if (mCommentResponse.total_number > 0) {
            //评论数大于0,显示红点
            mTvCommentCount.setVisibility(View.VISIBLE);
            mTvCommentCount.setText(String.valueOf(mCommentResponse.total_number));
        }

        mCommentList.addAll(mCommentResponse.data);
        mCommentAdapter.notifyDataSetChanged();

        if (!mCommentResponse.has_more) {
            mCommentAdapter.loadMoreEnd();
        } else {
            mCommentAdapter.loadMoreComplete();
        }


    }

    @Override
    public void onError() {
        mStateView.showRetry();
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.getComment(mGroupId, mItemId, mCommentList.size() / Constant.COMMENT_PAGE_SIZE + 1);
    }
}
