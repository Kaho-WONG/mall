package com.mars.mall.enums;

import lombok.Getter;

/**
 * @description: 支付方式枚举类  1-在线支付
 * @author: Mars
 * @create: 2021-10-05 15:52
 **/
@Getter
public enum PaymentTypeEnum {

    PAY_ONLINE(1),
    ;

    Integer code;

    PaymentTypeEnum(Integer code) {
        this.code = code;
    }
}
