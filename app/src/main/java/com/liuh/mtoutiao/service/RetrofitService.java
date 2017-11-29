package com.liuh.mtoutiao.service;



import com.liuh.mtoutiao.service.entity.Book;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by huan on 2017/11/14 08:51.
 */

public interface RetrofitService {
//https://api.douban.com/v2/book/search?q=西游记&tag=&start=0&count=1

    @GET("book/search")
    Observable<Book> getSearchBook(@Query("q") String name,
                                   @Query("tag") String tag,
                                   @Query("start") int start,
                                   @Query("count") int count);


}
