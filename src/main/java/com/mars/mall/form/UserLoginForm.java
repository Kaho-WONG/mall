package com.mars.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description: 表单，专门用一个form来作为传入controller的参数
 * UserLoginForm 用来封装登录所用的参数
 * @author: Mars
 * @create: 2021-09-27 20:00
 *
 * 知识点：
 * @Valid介绍及相关注解  主要用于表单验证,减轻了代码量
 * 在Springboot启动器的web包下包含了javax.validation.Valid所以无需添加多余的依赖
 * 使用方式：
 * 1.在相关的实体类(form)的相关字段添加用于充当验证条件的注解
 * 2.在controller层的方法的要校验的参数上添加@Valid注解
 * 3.编写全局异常捕捉类
 **/
@Data
public class UserLoginForm {
    //以下几个注解和@Valid配合使用
    //@NotBlank 用于 String 判断空格
    //@NotEmpty 用于集合
    //@NotNull
    @NotBlank
    private String username;//用户名
    @NotBlank
    private String password;//密码
}
