package com.mars.mall.form;

import lombok.Data;

/**
 * @description: 购物车更新商品参数表单
 * @author: Mars
 * @create: 2021-10-04 11:45
 **/
@Data
public class CartUpdateForm {

    private Integer quantity;

    private Boolean selected;
}
