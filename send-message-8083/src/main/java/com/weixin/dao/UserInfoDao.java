package com.weixin.dao;

import com.weixin.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserInfoDao {
    public List<UserInfo >findAll();
}
