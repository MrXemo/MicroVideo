package com.micro.microvideo.main.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by William on 2018/6/27.
 */

public class RoleBean implements Serializable {

    private String id;
    private String name;
    private BigDecimal one;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getOne() {
        return one;
    }

    public void setOne(BigDecimal one) {
        this.one = one;
    }
}
