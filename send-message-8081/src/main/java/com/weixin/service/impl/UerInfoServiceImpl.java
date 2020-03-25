package com.weixin.service.impl;

import com.weixin.dao.UserInfoDao;
import com.weixin.entity.UserInfo;
import com.weixin.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UerInfoServiceImpl implements UserInfoService {

    @Autowired
    public UserInfoDao userInfoDao;

    @Override
    public List<UserInfo> findAll() {
        return userInfoDao.findAll();
    }
}
