package com.mars.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description: 购物车添加商品的参数表单
 * @author: Mars
 * @create: 2021-10-02 15:44
 **/
@Data
public class CartAddForm {

    @NotNull
    private Integer productId;

    private Boolean selected = true;
}
