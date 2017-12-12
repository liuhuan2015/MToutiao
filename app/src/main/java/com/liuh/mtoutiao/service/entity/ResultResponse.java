package com.liuh.mtoutiao.service.entity;

/**
 * Date: 2017/12/12 09:58
 * Description: 访问返回的数据
 */

public class ResultResponse<T> {
    public String has_more;
    public String message;
    public String success;
    public T data;

    public ResultResponse(String has_more, String message, T data) {
        this.has_more = has_more;
        this.message = message;
        this.data = data;
    }
}
