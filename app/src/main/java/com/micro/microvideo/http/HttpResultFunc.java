package com.micro.microvideo.http;


import io.reactivex.functions.Function;

/**
 * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 *
 * @param <> Subscriber真正需要的数据类型，也就是Data部分的数据类型
 */

public class HttpResultFunc<T> implements Function<HttpResult<T>, T> {

   @Override
    public T apply(HttpResult<T> httpResult) {

        if (httpResult.getCode() != 200) {
            throw new ApiException(httpResult.getMessage());
        }
        return  httpResult.getData();
    }
}