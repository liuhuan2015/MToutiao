package com.liuh.mtoutiao.service.presenter;

import com.google.gson.Gson;
import com.liuh.mtoutiao.service.entity.News;
import com.liuh.mtoutiao.service.entity.NewsData;
import com.liuh.mtoutiao.service.response.NewsResponse;
import com.liuh.mtoutiao.ui.iview.INewsListView;
import com.liuh.mtoutiao.ui.utils.LogUtil;
import com.liuh.mtoutiao.ui.utils.PreUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 * Date: 2017/12/11 14:54
 * Description:
 */

public class NewsListPresenter extends BasePresenter<INewsListView> {

    private INewsListView mINewsListView;

    private long lastTime;

    private NewsResponse mNewsResponse;

    public NewsListPresenter(INewsListView view) {
        super(view);
        this.mINewsListView = view;
    }

    public void getNewsList(String channelCode) {
        lastTime = PreUtils.getLong(channelCode, 0);//读取对应频道最后一次刷新的时间戳
        if (lastTime == 0) {
            //如果为空，则是从来没有刷新过，使用当前时间戳
            lastTime = System.currentTimeMillis() / 1000;
        }

        DisposableObserver<NewsResponse> disposableObserver = new DisposableObserver<NewsResponse>() {
            @Override
            public void onNext(NewsResponse newsResponse) {
                mNewsResponse = newsResponse;
                lastTime = System.currentTimeMillis();
                PreUtils.putLong(channelCode, lastTime);
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e("NewsListPresenter", "....." + e.getLocalizedMessage());
                mINewsListView.onError();
            }

            @Override
            public void onComplete() {
                if (mNewsResponse != null) {
                    List<NewsData> data = mNewsResponse.data;
                    List<News> newsList = new ArrayList<>();
                    if (data != null && data.size() > 0) {
                        for (NewsData newsData : data) {
                            News news = new Gson().fromJson(newsData.content, News.class);
                            newsList.add(news);
                        }
                    }
                    mINewsListView.onGetNewsListSuccess(newsList, mNewsResponse.tips.display_info);
                }
            }
        };

        addDisposableObserver(apiService.getNewsList(channelCode, lastTime, System.currentTimeMillis()), disposableObserver);

    }


}
