package com.micro.microvideo.http;

/**
 * Created by Dell on 2016/9/9.
 */
public interface ApiCallback<T> {

    void onSuccess(T model);

    void onFailure(String msg);
}
