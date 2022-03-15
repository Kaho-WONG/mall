package com.mars.mall.controller;

import com.github.pagehelper.PageInfo;
import com.mars.mall.consts.MallConst;
import com.mars.mall.form.OrderCreateForm;
import com.mars.mall.pojo.User;
import com.mars.mall.service.IOrderService;
import com.mars.mall.vo.OrderVo;
import com.mars.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @description: 订单模块Controller层
 * @author: Mars
 * @create: 2021-10-06 16:03
 **/
@RestController
public class OrderController {

    @Autowired
    private IOrderService orderService;

    /**
     * 创建订单
     * @param form 包含了shippingId的请求表单
     * @param session 保存了当前登录的用户信息，可以拿到uid
     * @return
     */
    @PostMapping("/orders")
    public ResponseVo<OrderVo> create(@Valid @RequestBody OrderCreateForm form,
                                      HttpSession session){
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.create(user.getId(),form.getShippingId());
    }

    /**
     * 将指定用户的所有订单罗列成订单列表
     * @param pageNum 页码
     * @param pageSize 页容纳条目数
     * @param session 保存了当前登录的用户信息，可以拿到uid
     * @return
     */
    @GetMapping("/orders")
    public ResponseVo<PageInfo> list(@RequestParam Integer pageNum,
                                     @RequestParam Integer pageSize,
                                     HttpSession session){
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.list(user.getId(),pageNum,pageSize);
    }

    /**
     * 展示订单详情
     * @param orderNo 订单号
     * @param session 保存了当前登录的用户信息，可以拿到uid
     * @return
     */
    @GetMapping("/orders/{orderNo}")
    public ResponseVo<OrderVo> detail(@PathVariable Long orderNo,
                                      HttpSession session){
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.detail(user.getId(),orderNo);
    }

    /**
     * 取消订单
     * @param orderNo 订单号
     * @param session 保存了当前登录的用户信息，可以拿到uid
     * @return
     */
    @PutMapping("/orders/{orderNo}")
    public ResponseVo cancel(@PathVariable Long orderNo,
                             HttpSession session){
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.cancel(user.getId(),orderNo);
    }
}
