package com.mars.mall.dao;

import com.mars.mall.pojo.Order;


import java.util.List;

/**
 * 订单模块dao层
 */
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<Order> selectByUid(Integer uid);

    Order selectByOrderNo(Long orderNo);//通过订单号查询某个订单
}