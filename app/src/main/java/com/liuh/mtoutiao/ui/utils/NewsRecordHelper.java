package com.liuh.mtoutiao.ui.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liuh.mtoutiao.service.entity.News;
import com.liuh.mtoutiao.service.entity.NewsRecord;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Date: 2017/12/12 15:17
 * Description: 用于获取新闻记录
 */

public class NewsRecordHelper {

    private static Gson mGson = new Gson();

    /**
     * 获取数据库中保存的某一个频道的最后一条记录
     *
     * @param channelCode
     * @return
     */
    public static NewsRecord getLastNewsRecord(String channelCode) {
        return DataSupport.where("channelCode=?", channelCode).findLast(NewsRecord.class);
    }

    /**
     * 根据频道号码和页码查询新闻记录
     *
     * @param channelCode
     * @param page
     * @return
     */
    private static List<NewsRecord> selectNewsRecords(String channelCode, int page) {
        return DataSupport.where("channelCode=? and page=?", channelCode, String.valueOf(page))
                .find(NewsRecord.class);
    }

    /**
     * 获取某一个频道上一组新闻记录中的首条新闻记录
     *
     * @param channelCode 频道
     * @param page        页码
     * @return
     */
    public static NewsRecord getPreNewsRecord(String channelCode, int page) {
        List<NewsRecord> newsRecords = selectNewsRecords(channelCode, page - 1);
        if (newsRecords == null) return null;
        if (newsRecords.size() == 0) return null;
        return newsRecords.get(0);
    }

    /**
     * 保存新闻记录
     *
     * @param channelCode
     * @param json
     */
    public static void save(String channelCode, String json) {
        int page = 1;
        NewsRecord lastNewsRecord = getLastNewsRecord(channelCode);
        if (lastNewsRecord != null) {
            //如果有记录,页码为最后一条的页码加1
            page = lastNewsRecord.getPage() + 1;
        }

        //保存新的记录
        NewsRecord newsRecord = new NewsRecord(channelCode, page, json, System.currentTimeMillis());
        newsRecord.saveOrUpdate("channelCode=? and page=?", channelCode, String.valueOf(page));
    }

    /**
     * 将json转换成新闻集合
     *
     * @param json
     * @return
     */
    public static List<News> convertToNewsLits(String json) {
        return mGson.fromJson(json, new TypeToken<List<News>>() {
        }.getType());
    }

}
