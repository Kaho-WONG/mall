package com.mars.mall.service;

import com.github.pagehelper.PageInfo;
import com.mars.mall.vo.OrderVo;
import com.mars.mall.vo.ResponseVo;

/**
 * @description:
 * @author: Mars
 * @create: 2021-10-05 12:00
 **/
public interface IOrderService {

    ResponseVo<OrderVo> create(Integer uid,Integer shippingId);

    ResponseVo<PageInfo> list(Integer uid,Integer pageNum,Integer pageSize);

    ResponseVo<OrderVo> detail(Integer uid,Long orderNo);

    ResponseVo cancel(Integer uid,Long orderNo);

    void paid(Long orderNo);//修改订单状态为已付款

}
