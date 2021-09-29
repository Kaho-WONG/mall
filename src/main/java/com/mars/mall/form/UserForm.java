package com.mars.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description: 表单，专门用一个form来作为传入controller的参数
 * @author: Mars
 * @create: 2021-09-27 20:00
 **/
@Data
public class UserForm {

    //@NotBlank 用于 String 判断空格
    //@NotEmpty 用于集合
    //@NotNull
    @NotBlank
    private String username;//用户名
    @NotBlank
    private String password;//密码
    @NotBlank
    private String email;//邮箱
}
