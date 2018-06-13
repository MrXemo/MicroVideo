package com.micro.microvideo.http;

import java.util.List;

/**
 * Created by Dell on 2016/9/9.
 */
public interface ApiListCallback<T> {

    void onSuccess(List<T> model);

    void onFailure(String msg);
}
