package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service
public class CartServiceImpl implements DubboCartService{
	@Autowired
	private CartMapper cartMapper;
    //查询购物车
	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", userId);

		return cartMapper.selectList(queryWrapper);
	}
   //删除购物车商品
	@Override
	public void deleteCart(Cart cart) {
		//将对象中不为null的属性当做where条件
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>(cart);
		cartMapper.delete(queryWrapper);
	}
	//新增购物车商品
	@Override
	public void savaCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper=new QueryWrapper<Cart>();
		queryWrapper.eq("user_id",cart.getUserId() )
		            .eq("item_id", cart.getItemId());
		Cart cartDB=cartMapper.selectOne(queryWrapper);
		if(cartDB ==null) {
			//表示第一次新增
			cart.setCreated(new Date())
			    .setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {
			int num=cartDB.getNum()+cart.getNum();
			Cart cartTemp = new Cart();
			 cartTemp.setId(cartDB.getId())//设定主键
			        .setNum(num)
			        .setUpdated(new Date());
			 cartMapper.updateById(cartTemp);
		}
	}
	@Override
	public void updataCartNum(Cart cart) {
		// TODO Auto-generated method stub
		Cart cartTemp = new Cart();
		  cartTemp.setNum(cart.getNum())
		           .setUpdated(new Date());
		QueryWrapper<Cart> updateWrapper=new QueryWrapper<Cart>();
		updateWrapper.eq("user_id", cart.getUserId())
		             .eq("item_id", cart.getItemId());
		cartMapper.update(cartTemp, updateWrapper);
	}
}
