package com.liuh.mtoutiao.service.api;

import com.liuh.mtoutiao.service.entity.Book;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Date: 2017/12/12 09:05
 * Description:接口定义
 */

public interface ApiService {

    //https://api.douban.com/v2/book/search?q=西游记&tag=&start=0&count=1

    @GET("book/search")
    Observable<Book> getSearchBook(@Query("q") String name,
                                   @Query("tag") String tag,
                                   @Query("start") int start,
                                   @Query("count") int count);
}
