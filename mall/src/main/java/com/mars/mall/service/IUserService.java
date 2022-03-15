package com.mars.mall.service;

import com.mars.mall.pojo.User;
import com.mars.mall.vo.ResponseVo;

/**
 * @description: 用户模块Service层接口
 * @author: Mars
 * @create: 2021-09-26 23:10
 **/
public interface IUserService {
    /**
     * 注册
     */
    ResponseVo<User> register(User user);

    /**
     * 登录
     */
    ResponseVo<User> login(String username,String password);
}
