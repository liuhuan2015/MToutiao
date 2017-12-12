package com.liuh.mtoutiao.service.presenter;

import com.liuh.mtoutiao.service.api.ApiRetrofit;
import com.liuh.mtoutiao.service.api.ApiService;

import java.io.Serializable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by huan on 2017/11/14 10:38.
 */

public abstract class BasePresenter<V> {

    protected ApiService apiService = ApiRetrofit.getInstance().getmApiService();

    protected V mView;
    //CompositeDisposable是用来存放RxJava中的订阅关系,
    //请求完数据要及时清掉这个订阅关系，不然会发生内存泄漏
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public BasePresenter(V view) {
        attachView(view);
    }

    /**
     * 用于绑定我们自己的View
     *
     * @param view
     */
    public void attachView(V view) {
        mView = view;
    }

    /**
     * 请求完数据后，这个方法要及时调用，以发生内存泄漏
     */
    public void detachView() {
        mView = null;
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    public void addDisposableObserver(Observable<? extends Serializable> observable, DisposableObserver disposableObserver) {

        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }

        observable.subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);

        mCompositeDisposable.add(disposableObserver);
    }

}
