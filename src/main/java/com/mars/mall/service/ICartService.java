package com.mars.mall.service;

import com.mars.mall.form.CartAddForm;
import com.mars.mall.form.CartUpdateForm;
import com.mars.mall.pojo.Cart;
import com.mars.mall.vo.CartVo;
import com.mars.mall.vo.ResponseVo;

import java.util.List;

/**
 * @description:
 * @author: Mars
 * @create: 2021-10-02 19:36
 **/
public interface ICartService {

    ResponseVo<CartVo> add(Integer uid,CartAddForm cartAddForm);

    ResponseVo<CartVo> list(Integer uid);

    ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form);

    ResponseVo<CartVo> delete(Integer uid,Integer productId);

    ResponseVo<CartVo> selectAll(Integer uid);

    ResponseVo<CartVo> unSelectAll(Integer uid);

    ResponseVo<Integer> sum(Integer uid);

    List<Cart> listForCart(Integer uid);
}
