package com.mars.mall.enums;

import lombok.Getter;

/**
 * @description: 角色枚举类，用于注册新用户时设置其身份角色
 * 0——管理员
 * 1——顾客
 * @author: Mars
 * @create: 2021-09-27 00:03
 **/
@Getter
public enum RoleEnum {

    ADMIN(0),    //管理员

    CUSTOMER(1), //顾客
    ;
    Integer code;//角色状态码，0-管理员，1-顾客

    RoleEnum(Integer code) {
        this.code = code;
    }
}
