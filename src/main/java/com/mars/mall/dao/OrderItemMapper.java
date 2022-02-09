package com.mars.mall.dao;

import com.mars.mall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 订单条目模块dao层
 */
public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    int batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);

    //根据订单号集合查询这些订单号对应订单中的所有订单条目orderItem
    List<OrderItem> selectByOrderNoSet(@Param("orderNoSet") Set orderNoSet);
}