package com.liuh.mtoutiao.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuh.mtoutiao.service.entity.CommentData;

import java.util.List;

/**
 * Date: 2017/12/15 09:40
 * Description:新闻详情界面评论列表的适配器
 */

public class CommentAdapter extends BaseQuickAdapter<CommentData, BaseViewHolder> {

    private Context mContext;

    public CommentAdapter(Context context, int layoutResId, @Nullable List<CommentData> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentData item) {

    }
}
