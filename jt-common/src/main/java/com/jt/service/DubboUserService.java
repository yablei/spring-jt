package com.jt.service;

import com.jt.pojo.User;

//定义中立第三方接口
public interface DubboUserService {
	

	void savaUser(User user);

	String doLogin(User user);

	
}
