package com.mars.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description: 收货地址表单
 * @author: Mars
 * @create: 2021-10-04 14:36
 **/
@Data
public class ShippingForm {

    @NotBlank
    private String receiverName;//收货人

    @NotBlank
    private String receiverPhone;//收货人手机号

    @NotBlank
    private String receiverMobile;//收货人固定电话号

    @NotBlank
    private String receiverProvince;//省份

    @NotBlank
    private String receiverCity;//城市

    @NotBlank
    private String receiverDistrict;//区县

    @NotBlank
    private String receiverAddress;//详细地址

    @NotBlank
    private String receiverZip;//邮编
}
