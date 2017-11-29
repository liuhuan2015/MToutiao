package com.liuh.mtoutiao.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.liuh.mtoutiao.service.entity.Book;
import com.liuh.mtoutiao.service.presenter.BookPresenter;
import com.liuh.mtoutiao.ui.IView.BookView;
import com.liuh.mtoutiao.R;


public class MainActivity extends AppCompatActivity implements BookView {

    TextView tvContent;
    private BookPresenter mBookPresenter = new BookPresenter(this);

    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvContent = findViewById(R.id.tv_content);

        mBookPresenter.onCreate();
        mBookPresenter.attachView(this);
        mBookPresenter.getSearchBooks("西游记", null, 0, 1);
    }

    @Override
    public void onSuccess(Book book) {
        tvContent.setText(book.toString());
    }

    @Override
    public void onError(String result) {
        tvContent.setText(result);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();
}
