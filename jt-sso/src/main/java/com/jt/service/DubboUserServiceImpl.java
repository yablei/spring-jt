package com.jt.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserInfoMapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;
//生产者实现类
@Service//alibaba包
public class DubboUserServiceImpl implements DubboUserService{
  @Autowired
	private UserMapper userMapper;
  @Autowired
  private  JedisCluster jedisCluster;
  @Autowired
  private UserInfoMapper userinfoMapper;
	@Override
	public void savaUser(User user) {
		// TODO Auto-generated method stub
	   //1.密码加密
		//DigestUtils  工具API
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		//封装数据
		//暂时使用电话代替邮箱
		user.setEmail(user.getPhone()).setPassword(md5Pass).setCreated(new Date()).setUpdated(user.getCreated());
		userMapper.insert(user);
	}
	@Override
	public String doLogin(User user) {
		String md5Pass=DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		//将对象中不为null的属性当做where条件
	    QueryWrapper<User>	queryWrapper=new QueryWrapper<User>(user);
		User userDB = userMapper.selectOne(queryWrapper);//查找数据库
		String token=null;
		if(userDB !=null) {
			//将用户数据保存到Redis中 生成key
			String tokenTemp="JT_TICKET_"+System.currentTimeMillis()+user.getUsername();
			tokenTemp=DigestUtils.md5DigestAsHex(tokenTemp.getBytes());//将生成的key进行加密
			
			//生成value (json数据)
			//为了用户安全进行脱密处理(脱离敏感数据)
			userDB.setPassword("123456");
			String userjson = ObjectMapperUtil.toJSON(userDB);
			jedisCluster.setex(tokenTemp, 7*24*3600, userjson);//设置失效时间
			token=tokenTemp;
		}
		
		return token;
	}
	

}
