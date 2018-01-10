package com.liuh.mtoutiao.service.api;

import com.google.gson.GsonBuilder;
import com.liuh.mtoutiao.app.base.BaseApp;
import com.liuh.mtoutiao.app.constants.Constant;
import com.liuh.mtoutiao.ui.utils.LogUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Date: 2017/12/12 09:06
 * Description:
 */

public class ApiRetrofit {
    private static ApiRetrofit mApiRetrofit;
    private OkHttpClient mClient;
    private final Retrofit mRetrofit;
    private ApiService mApiService;

    private ApiRetrofit() {
        File httpCacheDirectory = new File(BaseApp.getmContext().getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        mClient = new OkHttpClient.Builder()
                .addInterceptor(mHeaderIntercepter)
                .addInterceptor(mLogInterceptor)
                .cache(cache)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//支持Rxjava
                .client(mClient)
                .build();
        mApiService = mRetrofit.create(ApiService.class);

    }

    public static ApiRetrofit getInstance() {
        if (mApiRetrofit == null) {
            synchronized (Object.class) {
                if (mApiRetrofit == null) {
                    mApiRetrofit = new ApiRetrofit();
                }
            }
        }
        return mApiRetrofit;
    }

    /**
     * 增加头部信息的拦截器
     */
    private Interceptor mHeaderIntercepter = chain -> {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.108 Safari/537.36 2345Explorer/8.0.0.13547");
        builder.addHeader("Cache-Control", "max-age=0");//缓存时间为0秒
        builder.addHeader("Upgrade-Insecure-Requests", "1");
        builder.addHeader("X-Requested-With", "XMLHttpRequest");
        builder.addHeader("Cookie", "uuid=\"w:f2e0e469165542f8a3960f67cb354026\"; __tasessionId=4p6q77g6q1479458262778; csrftoken=7de2dd812d513441f85cf8272f015ce5; tt_webid=36385357187");
        return chain.proceed(builder.build());
    };


    /**
     * 请求request和响应Response的拦截器
     */
    private Interceptor mLogInterceptor = chain -> {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        Response response = chain.proceed(chain.request());
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        LogUtil.e("ApiRetrofit", "---------------Request Start----------------");
        LogUtil.e("ApiRetrofit", "|...Request:" + request.toString());
        LogUtil.e("ApiRetrofit", "|...Response:" + content);
        LogUtil.e("ApiRetrofit", "---------------Request End----------------" + duration + "毫秒-------");
        return response.newBuilder()
                .body(ResponseBody.create(mediaType, content))
                .build();
    };

    /**
     * 对旧的oldRequest做了一堆处理，加入了一些通用的统计参数，包装成生成了一个新的newRequest，然后调用chain.proceed方法
     */
    private Interceptor mNetStatisticsInterceptor = (Interceptor.Chain chain) -> {
        Request request = chain.request();
        HttpUrl httpUrl = request.url();
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        if (httpUrl.queryParameter("app_version") == null) {
//            urlBuilder.addQueryParameter("app_version", BaseConfig.versionName);
            urlBuilder.addQueryParameter("app_version", "3.1");
        }
        // 用chain.request()构造一个新的传入统计参数的request，作为参数调用chain.proceed
        return chain.proceed(request.newBuilder().url(urlBuilder.build()).build());
    };

    public ApiService getmApiService() {
        return mApiService;
    }
}
