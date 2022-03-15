package com.mars.mall.controller;

import com.mars.mall.consts.MallConst;
import com.mars.mall.form.CartAddForm;
import com.mars.mall.form.CartUpdateForm;
import com.mars.mall.pojo.User;
import com.mars.mall.service.ICartService;
import com.mars.mall.vo.CartVo;
import com.mars.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @description: 购物车模块controller层
 * @author: Mars
 * @create: 2021-10-02 15:45
 **/
@RestController
public class CartController {

    @Autowired
    private ICartService cartService;

    @GetMapping("/carts")
    public ResponseVo<CartVo> list(HttpSession session){
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);//获取当前正在登录的用户
        return cartService.list(user.getId());
    }

    @PostMapping("/carts")
    public ResponseVo<CartVo> add(@Valid @RequestBody CartAddForm cartAddForm,
                                  HttpSession session){
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);//获取当前正在登录的用户
        return cartService.add(user.getId(),cartAddForm);
    }

    @PutMapping("/carts/{productId}")//@PutMapping主要用于更新请求
    public ResponseVo<CartVo> update(@PathVariable Integer productId,
                                     @Valid @RequestBody CartUpdateForm cartUpdateForm,
                                     HttpSession session){
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);//获取当前正在登录的用户
        return cartService.update(user.getId(),productId,cartUpdateForm);
    }

    @DeleteMapping("/carts/{productId}")//@DeleteMapping主要用于删除请求
    public ResponseVo<CartVo> delete(@PathVariable Integer productId, HttpSession session){
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);//获取当前正在登录的用户
        return cartService.delete(user.getId(),productId);
    }

    @PutMapping("/carts/selectAll")
    public ResponseVo<CartVo> selectAll(HttpSession session){
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);//获取当前正在登录的用户
        return cartService.selectAll(user.getId());
    }

    @PutMapping("/carts/unSelectAll")
    public ResponseVo<CartVo> unSelectAll(HttpSession session){
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);//获取当前正在登录的用户
        return cartService.unSelectAll(user.getId());
    }

    @GetMapping("/carts/products/sum")
    public ResponseVo<Integer> sum(HttpSession session){
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);//获取当前正在登录的用户
        return cartService.sum(user.getId());
    }
}
