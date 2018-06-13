package com.micro.microvideo.main.bean;

/**
 * Created by William on 2018/6/4.
 */

public class CommentBean {
    private String imgurl;
    private String username;
    private String remark;

    public CommentBean(String remark, String username) {
        this.remark = remark;
        this.username = username;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
