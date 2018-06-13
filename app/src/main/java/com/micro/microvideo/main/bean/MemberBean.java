package com.micro.microvideo.main.bean;

/**
 * Created by William on 2018/6/13.
 */

public class MemberBean {
    private String id;
    private String roleText;
    private Integer role_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleText() {
        return roleText;
    }

    public void setRoleText(String roleText) {
        this.roleText = roleText;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }
}
