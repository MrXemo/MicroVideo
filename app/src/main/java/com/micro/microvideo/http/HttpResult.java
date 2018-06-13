package com.micro.microvideo.http;

/**
 * Created by Dell on 2016/11/30.
 */

public class HttpResult<T>{
    private int code;
    private int total;
    private T data;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (null != data) {
            sb.append( data.toString());
        }
        return sb.toString();
    }
}
