package com.weixin.entity;

public class UserInfo {
    private Long id;
    private String openId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                '}';
    }
}
