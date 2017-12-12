package com.liuh.mtoutiao.service.presenter;


import com.liuh.mtoutiao.service.entity.Book;
import com.liuh.mtoutiao.ui.iview.BookView;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by huan on 2017/11/14 10:42.
 */

public class BookPresenter extends BasePresenter<BookView> {

    private BookView mBookView;
    private Book mBook;

    public BookPresenter(BookView view) {
        super(view);
        this.mBookView = view;
    }

    public void getSearchBooks(String name, String tag, int start, int count) {

        DisposableObserver<Book> disposableObserver = new DisposableObserver<Book>() {
            @Override
            public void onNext(Book book) {
                mBook = book;
            }

            @Override
            public void onError(Throwable e) {
                mBookView.onError("请求失败 " + e.getMessage());
            }

            @Override
            public void onComplete() {
                if (mBook != null) {
                    mBookView.onSuccess(mBook);
                }
            }
        };

        addDisposableObserver(apiService.getSearchBook(name, tag, start, count), disposableObserver);

    }

}
