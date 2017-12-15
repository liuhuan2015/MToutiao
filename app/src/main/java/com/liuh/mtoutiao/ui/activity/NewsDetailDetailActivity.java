package com.liuh.mtoutiao.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuh.mtoutiao.R;
import com.liuh.mtoutiao.service.entity.NewsDetail;
import com.liuh.mtoutiao.ui.utils.GlideUtils;
import com.liuh.mtoutiao.ui.utils.LogUtil;
import com.liuh.mtoutiao.ui.widget.NewsDetailHeaderView;

import butterknife.BindView;

/**
 * 非视频新闻详情
 */
public class NewsDetailDetailActivity extends NewsDetailBaseActivity {

    @BindView(R.id.ll_user)
    LinearLayout mLlUser;

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;

    @BindView(R.id.tv_author)
    TextView mTvAuthor;


    @Override
    protected int getViewContentViewId() {
        return R.layout.activity_news_detail_detail;
    }


    @Override
    protected void initListener() {
        super.initListener();
        int llInfoButton = mHeaderView.mLlInfo.getBottom();
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRvComment.getLayoutManager();

        mRvComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int position = layoutManager.findFirstVisibleItemPosition();
                View firstVisiableChildView = layoutManager.findViewByPosition(position);
                int itemHeight = firstVisiableChildView.getHeight();
                int scrollHeight = position * itemHeight - firstVisiableChildView.getTop();
                LogUtil.e("NewsDetailDetailActivity", "------------scrollHeight:" + scrollHeight + "----------llInfoButton:" + llInfoButton);
                //如果滚动超过用户信息一栏,显示标题栏中的用户头像和昵称
                mLlUser.setVisibility(scrollHeight > llInfoButton ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onGetNewsDetailSuccess(NewsDetail newsDetail) {
        mHeaderView.setDetail(newsDetail, new NewsDetailHeaderView.LoadWebListener() {
            @Override
            public void onLoadFinished() {
                //加载完成后,显示内容布局
                mStateView.showContent();
            }
        });
        mLlUser.setVisibility(View.GONE);

        if (newsDetail.media_user != null) {
            GlideUtils.loadRound(this, newsDetail.media_user.avatar_url, mIvAvatar);
            mTvAuthor.setText(newsDetail.media_user.screen_name);
        }
    }
}
