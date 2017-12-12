package com.liuh.mtoutiao.ui.iview;

import com.liuh.mtoutiao.service.entity.News;

import java.util.List;

/**
 * Date: 2017/12/11 14:48
 * Description:获取各个频道新闻列表数据的回调接口
 */

public interface INewsListView {

    void onGetNewsListSuccess(List<News> newsList, String tipInfo);

    void onError();

}
