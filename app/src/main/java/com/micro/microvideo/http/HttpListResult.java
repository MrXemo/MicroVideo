package com.micro.microvideo.http;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Dell on 2016/11/30.
 */

public class HttpListResult<T>{
    private int code;
    private int total;
    private int pageSize;
    private int pageNumber;
    private List<T> data;
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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNumber() {
        BigDecimal pageNumberi = new BigDecimal(total).divide(new BigDecimal(pageSize),BigDecimal.ROUND_UP,0);
        return pageNumber = pageNumberi.intValue();
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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
