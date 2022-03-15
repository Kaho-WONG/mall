package com.mars.mall.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户类 —— 用户表
 */
@Data
@NoArgsConstructor //lombok注解：为类提供一个无参的构造方法
public class User {
    private Integer id;//用户id

    private String username;//用户名

    private String password;//密码

    private String email;//邮箱

    private String phone;//电话号码

    private String question;//密保问题

    private String answer;//密保问题答案

    private Integer role;//用户角色 0——管理员 1——顾客

    private Date createTime;//用户创建时间

    private Date updateTime;//用户更新时间

    //这里单独设置一个构造器用于注册用户时传入必要项(用户名、密码、邮箱、角色)
    public User(String username, String password, String email, Integer role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}