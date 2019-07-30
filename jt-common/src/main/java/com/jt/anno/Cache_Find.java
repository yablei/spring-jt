package com.jt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//查询注解

import com.jt.enu.KEY_ENUM;
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD}) //注解的使用范围   范围是在方法中
public @interface Cache_Find {
  
	String key()  default ""; //接收用户key值 
	KEY_ENUM keyType() default KEY_ENUM.AUTO; //定义key类型
	int secondes()   default 0;  //设置失效时间 永不失效
	
}
