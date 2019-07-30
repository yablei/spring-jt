package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

@Service
public class OrderServiceImpl implements DubboOrderService {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;

	@Transactional
	@Override
	public String savaOrder(Order order) {
		// TODO Auto-generated method stub
		String orderId = System.currentTimeMillis() + "" + order.getUserId();
		Date now = new Date();
		// 订单入库
		order.setOrderId(orderId);
		order.setStatus(1);
		order.setCreated(now);
		order.setUpdated(now);
		orderMapper.insert(order);
		System.out.println("订单入库成功");

		// 订单物流入库
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(now);
		orderShipping.setUpdated(now);
		orderShippingMapper.insert(orderShipping);
		System.out.println("订单物流入库成功");

		// 订单商品入库
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId);
			orderItem.setCreated(now);
			orderItem.setUpdated(now);
			orderItemMapper.insert(orderItem);
		}
		System.out.println("订单商品入库成功");
		return orderId;
	}					

	@Override
	public Order findOrderById(String id) {
		Order order = orderMapper.selectById(id);
		OrderShipping shipping = orderShippingMapper.selectById(id);
		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("order_id", id);
		List<OrderItem> items = orderItemMapper.selectList(queryWrapper);
		order.setOrderItems(items).setOrderShipping(shipping);
		return order;
	}

}
