package com.liuh.mtoutiao.service.presenter;

import com.liuh.mtoutiao.service.entity.Book;
import com.liuh.mtoutiao.ui.iview.INewsListView;

import io.reactivex.observers.DisposableObserver;

/**
 * Date: 2017/12/11 14:54
 * Description:
 */

public class NewsListPresenter extends BasePresenter<INewsListView> {

    private INewsListView mINewsListView;

    public NewsListPresenter(INewsListView view) {
        super(view);
        this.mINewsListView = view;
    }

}
