package com.micro.microvideo.http;

/**
 * Created by Dell on 2016/9/9.
 */
public interface ApiPageCallback<T> {

    void onSuccess(HttpListResult<T> model);

    void onFailure(String msg);
}
