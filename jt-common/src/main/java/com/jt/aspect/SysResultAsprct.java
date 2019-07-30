package com.jt.aspect;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jt.vo.SysResult;

import lombok.extern.slf4j.Slf4j;
//@ControllerAdvice  //针对controller层生效
@RestControllerAdvice
@Slf4j
public class SysResultAsprct {
   /*
           * 如果程序报错,统一返回系统异常
    *SysResult.fail()
    */
	@ExceptionHandler({RuntimeException.class})//如果遇到指定异常执行以下方法
	
	public SysResult SysResultFail(Exception e) {
		
		e.printStackTrace();
		log.error("服务器异常信息:"+e.getMessage());
		return SysResult.fail();
	}
      	
}
