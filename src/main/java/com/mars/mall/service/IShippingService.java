package com.mars.mall.service;

import com.github.pagehelper.PageInfo;
import com.mars.mall.form.ShippingForm;
import com.mars.mall.vo.ResponseVo;

import java.util.Map;

/**
 * @description:
 * @author: Mars
 * @create: 2021-10-04 14:39
 **/
public interface IShippingService {

    ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm form);//添加用户收货地址

    ResponseVo delete(Integer uid, Integer shippingId);//删除用户的收货地址

    ResponseVo update(Integer uid, Integer shippingId, ShippingForm form);//更新用户的收货地址

    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);//列出用户收货地址
}
