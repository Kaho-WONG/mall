package com.mars.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description: 创建订单用到的请求表单，包含了收货地址id
 * @author: Mars
 * @create: 2021-10-06 16:04
 **/
@Data
public class OrderCreateForm {

    @NotNull
    private Integer shippingId;
}
