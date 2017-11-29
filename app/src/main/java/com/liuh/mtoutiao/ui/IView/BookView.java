package com.liuh.mtoutiao.ui.IView;


import com.liuh.mtoutiao.service.entity.Book;

/**
 * Created by huan on 2017/11/14 10:36.
 */

public interface BookView extends MView {
    void onSuccess(Book book);

    void onError(String result);

}
