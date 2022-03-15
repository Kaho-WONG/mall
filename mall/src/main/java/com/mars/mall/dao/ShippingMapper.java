package com.mars.mall.dao;

import com.mars.mall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 收货地址模块dao层
 */
public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByIdAndUid(@Param("uid") Integer uid,
                         @Param("shippingId") Integer shippingId);

    List<Shipping> selectByUid(Integer uid);

    //服务于订单模块
    Shipping selectByUidAndShippingId(@Param("uid") Integer uid,
                                      @Param("shippingId") Integer shippingId);

    //通过shippingId集合查询数据库Shipping表所有的收货地址Shipping
    List<Shipping> selectByIdSet(@Param("idSet") Set idSet);
}