package com.liuh.mtoutiao.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.liuh.mtoutiao.R;
import com.liuh.mtoutiao.service.entity.News;
import com.liuh.mtoutiao.ui.utils.ListUtils;
import com.liuh.mtoutiao.ui.utils.TimeUtils;

import java.util.List;

/**
 * Date: 2017/12/12 14:34
 * Description:新闻列表的数据适配器
 */

public class NewsApdater extends BaseQuickAdapter<News, BaseViewHolder> {

    /**
     * 纯文字布局(文章,广告)
     */
    private static final int TEXT_NEWS = 100;

    /**
     * 居中大图布局(1,单图文章;2,单图广告;3,视频,中间显示播放图标,右侧显示时长)
     */
    private static final int CENTER_SINGLE_PIC_NEWS = 200;
    /**
     * 右侧小图布局(1,小图新闻;2,视频类型,右下角显示视频时长)
     */
    private static final int RIGHT_PIC_VIDEO_NEWS = 300;
    /**
     * 三张图片布局(文章,广告)
     */
    private static final int THREE_PICS_NEWS = 400;
    /**
     * 视频列表类型
     */
    private static final int VIDEO_LIST_NEWS = 500;

    private Context mContext;

    private String mChannelCode;

    private boolean isVideoList;

    /**
     * @param context     上下文
     * @param channelCode 频道
     * @param isVideoList 是否是视频列表
     * @param data        新闻集合
     */
    public NewsApdater(Context context, String channelCode, boolean isVideoList, @Nullable List<News> data) {
        super(data);
        mContext = context;
        mChannelCode = channelCode;
        this.isVideoList = isVideoList;


        setMultiTypeDelegate(new MultiTypeDelegate<News>() {
            @Override
            protected int getItemType(News news) {
                if (isVideoList) {
                    return VIDEO_LIST_NEWS;
                }

                if (news.has_video) {
                    //如果有视频
                    if (news.video_style == 0) {
                        //右侧视频
                        if (news.middle_image == null || TextUtils.isEmpty(news.middle_image.url)) {
                            return TEXT_NEWS;
                        }
                        return RIGHT_PIC_VIDEO_NEWS;
                    } else if (news.video_style == 2) {
                        //居中视频
                        return CENTER_SINGLE_PIC_NEWS;
                    }
                } else {
                    //非视频新闻
                    if (!news.has_image) {
                        //纯文字新闻
                        return TEXT_NEWS;
                    } else {
                        if (ListUtils.isEmpty(news.image_list)) {
                            //图片列表为空,则是右侧图片
                            return RIGHT_PIC_VIDEO_NEWS;
                        }

                        if (news.gallary_image_count == 3) {
                            //三张图
                            return THREE_PICS_NEWS;
                        }
                        //中间大图,右下角显示图片数量
                        return CENTER_SINGLE_PIC_NEWS;
                    }
                }
                return TEXT_NEWS;
            }
        });

        getMultiTypeDelegate()
                .registerItemType(TEXT_NEWS, R.layout.item_text_news)//纯文字布局
                .registerItemType(CENTER_SINGLE_PIC_NEWS, R.layout.item_center_pic_news)//居中大图布局
                .registerItemType(RIGHT_PIC_VIDEO_NEWS, R.layout.item_pic_video_news)//右侧小图布局
                .registerItemType(THREE_PICS_NEWS, R.layout.item_three_pics_news)//三张图片布局
                .registerItemType(VIDEO_LIST_NEWS, R.layout.item_video_list);//视频列表类型
    }

    @Override
    protected void convert(BaseViewHolder helper, News news) {
        if (TextUtils.isEmpty(news.title)) {
            //如果没有标题,则直接跳过
            return;
        }

        //设置标题,底部作者,评论数,发表时间
        if (!isVideoList) {
            //如果不是视频列表
            helper.setText(R.id.tv_title, news.title)
                    .setText(R.id.tv_author, news.source)
                    .setText(R.id.tv_comment_num, news.comment_count + "评论")
                    .setText(R.id.tv_time, TimeUtils.getShortTime(news.behot_time * 1000));
        }

        //根据类型为不同布局的条目设置数据





    }


}
