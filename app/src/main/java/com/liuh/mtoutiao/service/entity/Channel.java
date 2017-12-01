package com.liuh.mtoutiao.service.entity;

/**
 * Author:liuh
 * Date: 2017/12/1 10:56
 * Description:
 */

public class Channel {

    public static final int TYPE_MY = 1;
    public static final int TYPE_OTHER = 2;

    public String title;
    public String channelCode;
    private int channelType;


    public Channel(String title, String channelCode) {
        this.title = title;
        this.channelCode = channelCode;
    }


    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }
}
