package com.liuh.mtoutiao.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuh.mtoutiao.service.entity.News;

import java.util.List;

/**
 * Date: 2017/12/12 14:34
 * Description:新闻列表的数据适配器
 */

public class NewsApdater extends BaseQuickAdapter<News, BaseViewHolder> {

    public NewsApdater(Context context, String channelCode, boolean isVideoList, @Nullable List<News> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, News item) {

    }
}
