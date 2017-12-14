package com.liuh.mtoutiao.ui.utils;

import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.liuh.mtoutiao.service.api.ApiRetrofit;
import com.liuh.mtoutiao.service.response.VideoPathResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Date: 2017/12/14 10:42
 * Description:视频播放路径解析类
 */

public abstract class VideoPathDecoder {

    private static final String NICK = "liuh";

    public void decodePath(String srcUrl) {
        WebView webView = new WebView(UIUtils.getContext());

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//设置JS可用
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        ParseRelation relation = new ParseRelation(new IGetParamsListener() {
            @Override
            public void onGetParams(String r, String s) {
                sendRequest(srcUrl, r, s);
            }
        });

        webView.addJavascriptInterface(relation, NICK);

        webView.loadUrl("file:///android_asset/parse.html");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:getParseParam('" + srcUrl + "')");
            }
        });

    }

    private void sendRequest(String srcUrl, String r, String s) {
        ApiRetrofit.getInstance().getmApiService().getVideoPath(srcUrl, r, s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<VideoPathResponse>() {
                    @Override
                    public void onNext(VideoPathResponse videoPathResponse) {
                        LogUtil.e("VideoPathDecoder", "..........videoPathResponse:" + videoPathResponse);
                        String url = "";
                        switch (videoPathResponse.retCode) {
                            case 200:
                                //请求成功
                                List<VideoPathResponse.DataBean.VideoBean.DownloadBean> downloadBeanList = videoPathResponse.data.video.download;
                                if (!ListUtils.isEmpty(downloadBeanList)) {
                                    url = downloadBeanList.get(downloadBeanList.size() - 1).url;//取下载地址中最后一个地址,即超清
                                    LogUtil.e("VideoPathDecoder", "..........videoUrl:" + url);
                                }
                                onSuccess(url);
                                break;
                            case 400:
                                decodePath(srcUrl);//如果请求失败,可能是生成的r,s请求参数不正确,重新再调用
                                break;


                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("VideoPathDecoder", "..........Throwable:" + e.getLocalizedMessage());
                        onDecodeError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    protected abstract void onSuccess(String url);

    protected abstract void onDecodeError();

    private class ParseRelation {
        IGetParamsListener mListener;

        public ParseRelation(IGetParamsListener mListener) {
            this.mListener = mListener;
        }

        @JavascriptInterface
        public void onReceiveParams(String r, String s) {
            mListener.onGetParams(r, s);
        }

    }

    public interface IGetParamsListener {
        void onGetParams(String r, String s);
    }

}
