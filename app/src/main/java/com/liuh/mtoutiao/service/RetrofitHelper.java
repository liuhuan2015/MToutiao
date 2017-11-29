package com.liuh.mtoutiao.service;

import android.content.Context;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by huan on 2017/11/14 10:02.
 */

public class RetrofitHelper {
    private Context mContext;
    OkHttpClient mOkHttpClient = new OkHttpClient();
    private Retrofit mRetrofit = null;


    private static RetrofitHelper instance;

    private RetrofitHelper(Context context) {
        this.mContext = context;
        init();
    }

    public static RetrofitHelper getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitHelper(context);
        }

        return instance;
    }

    private void init() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.douban.com/v2/")
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//支持Rxjava
                .build();
    }

    public RetrofitService getService() {
        return mRetrofit.create(RetrofitService.class);
    }
}
