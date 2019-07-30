package com.jt.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.jt.mapper.UserInfoMapper;
import com.jt.pojo.UserInfo;
@Service
public class UserInfoServiceImpl implements UserInfoService {
   @Autowired
	private UserInfoMapper UserinfoMapper;
	
	@Override
	public void savaUserInfo(UserInfo userInfo) {
		// TODO Auto-generated method stub
   
	}

}
