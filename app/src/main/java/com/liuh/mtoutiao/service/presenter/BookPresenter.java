package com.liuh.mtoutiao.service.presenter;

import android.content.Context;
import android.content.Intent;

import com.liuh.mtoutiao.service.entity.Book;
import com.liuh.mtoutiao.service.manager.DataManager;
import com.liuh.mtoutiao.ui.IView.BookView;
import com.liuh.mtoutiao.ui.IView.MView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by huan on 2017/11/14 10:42.
 */

public class BookPresenter implements BasePresenter {
    private DataManager mDataManager;
    private Context mContext;
    private BookView mBookView;
    private Book mBook;
    //CompositeSubscription是用来存放RxJava中的订阅关系,
    //请求完数据要及时清掉这个订阅关系，不然会发生内存泄漏
    private CompositeSubscription mCompositeSubscription;


    public BookPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        mDataManager = new DataManager(mContext);
        mCompositeSubscription = new CompositeSubscription();

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(MView view) {
        mBookView = (BookView) view;
    }

    @Override
    public void detachView() {
        mBookView = null;
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    public void getSearchBooks(String name, String tag, int start, int count) {
        mCompositeSubscription.add(mDataManager.getSearchBooks(name, tag, start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Book>() {
                    @Override
                    public void onCompleted() {
                        if (mBook != null) {
                            mBookView.onSuccess(mBook);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mBookView.onError("请求失败 " + e.getMessage());
                    }

                    @Override
                    public void onNext(Book book) {
                        mBook = book;
                    }
                }));

    }

}
