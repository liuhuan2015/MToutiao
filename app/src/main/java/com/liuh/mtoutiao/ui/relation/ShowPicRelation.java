package com.liuh.mtoutiao.ui.relation;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.liuh.mtoutiao.ui.activity.ImageViewPagerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2017/12/15 11:14
 * Description:展示图片
 */

public class ShowPicRelation {
    private static final String TAG = ShowPicRelation.class.getSimpleName();

    private Context mContext;
    private List<String> mUrls = new ArrayList();

    public ShowPicRelation(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * JS中点击图片执行的java代码
     *
     * @param url
     */
    @JavascriptInterface
    public void openImg(String url) {
        //传递数据到展示图片的viewpager
        Intent intent = new Intent(mContext, ImageViewPagerActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 页面加载时JS调用的java代码
     *
     * @param urlArray
     */
    @JavascriptInterface
    public void getImgArray(String urlArray) {

        String[] urls = urlArray.split(";");//url拼接成的字符串,有分号隔开
        for (String url : urls) {
            mUrls.add(url);
        }
    }


}
