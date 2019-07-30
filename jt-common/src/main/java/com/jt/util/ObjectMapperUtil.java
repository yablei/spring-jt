package com.jt.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {
 
	private static final ObjectMapper mapper=new ObjectMapper();
	//将对象转化为JSON
	public static String toJSON(Object target) {
		String result=null;
		try {
			result=mapper.writeValueAsString(target);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}
	//将JSON转化为对象
	public static <T> T toObject(String json,Class<T> targetClass) {
		T t=null;
		try {
			t=mapper.readValue(json, targetClass);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return t;
	}
}
