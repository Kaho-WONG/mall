package com.mars.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mars.mall.dao.ShippingMapper;
import com.mars.mall.enums.ResponseEnum;
import com.mars.mall.form.ShippingForm;
import com.mars.mall.pojo.Shipping;
import com.mars.mall.service.IShippingService;
import com.mars.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 收货地址模块Service层
 * 商城有多个用户，每个用户都有多个收货地址
 * @author: Mars
 * @create: 2021-10-04 14:42
 **/
@Service
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    /**
     * 为指定用户添加新的收货地址
     * @param uid 用户id
     * @param form 收货地址信息表单
     * @return
     */
    @Override
    public ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm form) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(form,shipping);
        shipping.setUserId(uid);
        int row = shippingMapper.insert(shipping);//返回的是往数据库插入数据后影响的行数
        if (row <= 0){//服务端错误
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        Map<String,Integer> map = new HashMap<>();
        map.put("shippingId",shipping.getId());

        return ResponseVo.success(map);
    }

    /**
     * 根据地址id删除用户的某个收货地址
     * @param uid 用户id
     * @param shippingId 地址id
     * @return
     */
    @Override
    public ResponseVo delete(Integer uid, Integer shippingId) {
        int row = shippingMapper.deleteByIdAndUid(uid, shippingId);
        if (row <= 0){//服务端错误
            return ResponseVo.error(ResponseEnum.DELETE_SHIPPING_FAIL);
        }
        return ResponseVo.success();
    }

    /**
     * 更新用户的某个收货地址
     * @param uid 用户id
     * @param shippingId 地址id
     * @param form 携带被更新数据的收货地址信息表单
     * @return
     */
    @Override
    public ResponseVo update(Integer uid, Integer shippingId, ShippingForm form) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(form, shipping);
        shipping.setUserId(uid);
        shipping.setId(shippingId);
        int row = shippingMapper.updateByPrimaryKeySelective(shipping);
        if (row <= 0) {//服务端错误
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }

    /**
     * 罗列出指定用户的一个或多个收货地址信息，使用到分页插件
     * @param uid 用户id
     * @param pageNum 页码
     * @param pageSize 一页能容纳的条目数
     * @return
     */
    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippings = shippingMapper.selectByUid(uid);
        PageInfo pageInfo = new PageInfo(shippings);//将收货地址列表封装成PageInfo对象
        return ResponseVo.success(pageInfo);
    }
}
