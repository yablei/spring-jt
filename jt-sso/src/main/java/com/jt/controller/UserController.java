package com.jt.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.service.UserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private  JedisCluster jedisCluster;
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkuser(@PathVariable String param,@PathVariable Integer type,String callback) {
		JSONPObject object=null;
		try {
			//查询数据库,检查数据是否存在
			boolean flag=userService.checkuser(param,type);
			object=new JSONPObject(callback,SysResult.success(flag));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			object=new JSONPObject(callback,SysResult.fail());
		}
		return object;
		
	}
	@RequestMapping("/query/{token}")
	public JSONPObject findUserByToken(String callback,@PathVariable String token) {
		//1.根据秘钥查询用户信息
		String userJSON = jedisCluster.get(token);
		JSONPObject jsonpObject = null;
		if(StringUtils.isEmpty(userJSON)) {
			jsonpObject = new JSONPObject(callback,SysResult.fail());
		}else {
			//2.表示用户数据获取成功
			jsonpObject = new JSONPObject(callback,SysResult.success(userJSON));
		}
		return jsonpObject;
	}

}
