package com.jt.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.util.ObjectMapperUtil;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisCluster;

@Component
@Aspect
@Slf4j
public class RedisAspect {
	@Autowired(required = false) //用时才注入对象
	private JedisCluster jedisCluster;
   @Around(value="@annotation(cache_Find)")
	public Object findcat(ProceedingJoinPoint joinPoint,Cache_Find cache_Find) {
		//1.动态获取key
	   String key=getKey(joinPoint,cache_Find);
	   //2.从redis中获取数据
	   String resultJSON = jedisCluster.get(key);
	   Object resultData=null;
	   try {
		//3.判断数据是否有值
		   if(StringUtils.isEmpty(resultJSON)) {
			   //3.1表示缓存中没有数据,就查询数据库(调用业务方法)
			 resultData = joinPoint.proceed();
			 //3.2将数据保存到缓存中
			 String json = ObjectMapperUtil.toJSON(resultData);
			 //3.3判断当前数据是否有失效时间
			 if(cache_Find.secondes()==0) {
				 jedisCluster.set(key, json);
			 }else {
				 jedisCluster.setex(key, cache_Find.secondes(), json);
			 }
			 System.out.println("AOP查询数据库");
		   }else {
			   //4.表示Redis中有数据
			   Class returnType=getClass(joinPoint);
			  resultData = ObjectMapperUtil.toObject(resultJSON, returnType);
			  System.out.println("AOP查询缓存");
		   }
		   
	} catch (Throwable e) {
		// TODO: handle exception
		e.printStackTrace();
		log.error(e.getMessage());
		throw new RuntimeException();
	}
	return resultData;
	}

private Class getClass(ProceedingJoinPoint joinPoint) {
	// TODO Auto-generated method stub
	MethodSignature signature = (MethodSignature) joinPoint.getSignature();
	return null;
}
/*
 * key的定义规则如下:
 * 	1.如果用户使用AUTO.则自动生成KEY 方法名_第一个参数
 * 	2.如果用户使用EMPTY,使用用户自己的key
 */

private String getKey(ProceedingJoinPoint joinPoint, Cache_Find cache_Find) {
	// TODO Auto-generated method stub
	//1.判断用户选择的类型
	if(KEY_ENUM.EMPTY.equals(cache_Find.keyType())) {
		return cache_Find.key();
	}
	//2.表示用户动态生成key
	String methodName = joinPoint.getSignature().getName();
	String arg0 = String.valueOf(joinPoint.getArgs()[0]);
	return methodName+"::"+arg0;
  }
}
