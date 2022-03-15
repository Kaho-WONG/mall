package com.mars.mall.pojo;

import lombok.Data;

/**
 * @description: 用来存储在redis的对象，只需要三个属性：商品id、加入商品数量、商品是否被选中
 * @author: Mars
 * @create: 2021-10-03 00:20
 **/
@Data
public class Cart {

    private Integer productId;

    private Integer quantity;

    private Boolean productSelected;

    public Cart() {
    }

    public Cart(Integer productId, Integer quantity, Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productSelected = productSelected;
    }
}
