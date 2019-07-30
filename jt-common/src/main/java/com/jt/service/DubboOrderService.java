package com.jt.service;

import com.jt.pojo.Order;

public interface DubboOrderService {

	String savaOrder(Order order);

	Order findOrderById(String id);

}
