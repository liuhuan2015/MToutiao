package com.liuh.mtoutiao.service.presenter;

import com.liuh.mtoutiao.app.constants.Constant;
import com.liuh.mtoutiao.service.entity.NewsDetail;
import com.liuh.mtoutiao.service.response.CommentResponse;
import com.liuh.mtoutiao.service.response.ResultResponse;
import com.liuh.mtoutiao.ui.iview.INewsDetailView;

import io.reactivex.observers.DisposableObserver;

/**
 * Date: 2017/12/15 08:55
 * Description:获取新闻详情数据的presenter
 */

public class NewsDetailPresenter extends BasePresenter<INewsDetailView> {

    public NewsDetailPresenter(INewsDetailView view) {
        super(view);
    }

    public void getNewsDetail(String mDetailUrl) {
        addDisposableObserver(apiService.getNewsDetail(mDetailUrl), new DisposableObserver<ResultResponse<NewsDetail>>() {
            @Override
            public void onNext(ResultResponse<NewsDetail> newsDetailResultResponse) {
                mView.onGetNewsDetailSuccess(newsDetailResultResponse.data);
            }

            @Override
            public void onError(Throwable e) {
                mView.onError();
            }

            @Override
            public void onComplete() {

            }


//            @Override
//            public void onNext(NewsDetail newsDetail) {
//                mView.onGetNewsDetailSuccess(newsDetail);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                mView.onError();
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
        });
    }


    public void getComment(String mGroupId, String mItemId, int pageNow) {
        int offset = (pageNow - 1) * Constant.COMMENT_PAGE_SIZE;
        addDisposableObserver(apiService.getComment(mGroupId, mItemId, offset + "",
                String.valueOf(Constant.COMMENT_PAGE_SIZE)), new DisposableObserver<CommentResponse>() {

            @Override
            public void onNext(CommentResponse commentResponse) {
                mView.onGetCommentSuccess(commentResponse);
            }

            @Override
            public void onError(Throwable e) {
                mView.onError();
            }

            @Override
            public void onComplete() {

            }
        });

    }
}
