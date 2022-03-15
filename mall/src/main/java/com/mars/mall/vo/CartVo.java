package com.mars.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description: 购物车Vo
 * @author: Mars
 * @create: 2021-10-02 15:24
 **/
@Data
public class CartVo {

    private List<CartProductVo> cartProductVoList; //购物车存放的商品列表

    private Boolean selectedAll; //全选

    private BigDecimal cartTotalPrice; //购物车商品总价

    private Integer cartTotalQuantity; //购物车商品数量
}
