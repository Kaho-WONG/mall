package com.mars.mall.enums;

import lombok.Getter;

/**
 * 状态status枚举类
 */
@Getter
public enum ResponseEnum {

    ERROR(-1,"服务端错误"),

    SUCCESS(0,"成功"),

    PASSWORD_ERROR(1,"密码错误"),

    USERNAME_EXIST(2,"用户名已存在"),

    PARAM_ERROR(3,"参数错误"),

    EMAIL_EXIST(4,"邮箱已存在"),

    NEED_LOGIN(10,"用户未登录，请先登录"),

    ;

    Integer code;//状态码
    String desc;//状态描述信息

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
