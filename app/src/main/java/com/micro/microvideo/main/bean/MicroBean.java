package com.micro.microvideo.main.bean;

/**
 * Created by William on 2018/6/2.
 */

public class MicroBean {
    private String imgurl;
    private String name;

    public MicroBean() {
    }

    public MicroBean(String url, String name) {
        this.imgurl = url;
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
