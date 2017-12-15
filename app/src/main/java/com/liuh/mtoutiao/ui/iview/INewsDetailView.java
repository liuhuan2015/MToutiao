package com.liuh.mtoutiao.ui.iview;

import com.liuh.mtoutiao.service.entity.NewsDetail;
import com.liuh.mtoutiao.service.response.CommentResponse;

/**
 * Date: 2017/12/15 08:48
 * Description:新闻详情
 */

public interface INewsDetailView extends MView {

    void onGetNewsDetailSuccess(NewsDetail newsDetail);

    void onGetCommentSuccess(CommentResponse response);

    void onError();

}
