package com.liuh.mtoutiao.service.presenter;

import android.content.Intent;

import com.liuh.mtoutiao.ui.IView.MView;


/**
 * Created by huan on 2017/11/14 10:38.
 */

public interface BasePresenter {

    void onCreate();

    void onStart();

    void onStop();

    void onPause();

    /**
     * 用于绑定我们自己的View
     *
     * @param view
     */
    void attachView(MView view);

    void detachView();

    void attachIncomingIntent(Intent intent);

}
