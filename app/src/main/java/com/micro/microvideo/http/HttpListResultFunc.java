package com.micro.microvideo.http;


import java.util.List;

import io.reactivex.functions.Function;

/**
 * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 *BottomBarTab
 * @param <> Subscriber真正需要的数据类型，也就是Data部分的数据类型
 */

public class HttpListResultFunc<T> implements Function<HttpListResult<T>, List<T>> {

   @Override
    public List<T> apply(HttpListResult<T> httpResult) {

        if (httpResult.getCode() != 200) {
            throw new ApiException(httpResult.getMessage());
        }
        return  httpResult.getData();
    }
}