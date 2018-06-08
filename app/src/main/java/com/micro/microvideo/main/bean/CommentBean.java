package com.micro.microvideo.main.bean;

/**
 * Created by William on 2018/6/4.
 */

public class CommentBean {
    private String url;
    private String name;
    private String comment;

    public CommentBean(String comment) {
        this.comment = comment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
