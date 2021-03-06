package com.liuh.mtoutiao.app.constants;

/**
 * Author:liuh
 * Date: 2017/12/1 11:08
 * Description:一些全局的常量数值
 */

public class Constant {

    /**
     * 接口根地址
     */
    public static final String BASE_SERVER_URL = "http://is.snssdk.com/";
    /**
     * 已选中频道的json
     **/
    public static final String SELECTED_CHANNEL_JSON = "selectedChannelJson";
    /**
     * 未选中频道的json
     **/
    public static final String UNSELECTED_CHANNEL_JSON = "unselectedChannelJson";

    /**
     * 频道对应的请求参数
     */
    public static final String CHANNEL_CODE = "channelCode";
    public static final String IS_VIDEO_LIST = "isVideoList";

    public static final String ARTICLE_GENRE_VIDEO = "video";
    public static final String ARTICLE_GENRE_AD = "ad";

    public static final String TAG_MOVIE = "video_movie";

    public static final String URL_VIDEO = "/video/urls/v/1/toutiao/mp4/%s?r=%s";
    /**
     * 评论列表每页的数目
     */
    public static final int COMMENT_PAGE_SIZE = 20;
}
