package com.mars.mall.service.impl;

import com.mars.mall.MallApplicationTests;
import com.mars.mall.enums.ResponseEnum;
import com.mars.mall.enums.RoleEnum;
import com.mars.mall.pojo.User;
import com.mars.mall.service.IUserService;
import com.mars.mall.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Transactional //在测试类中加入这个事务注解，可以回滚，这样所测试的数据就不会被真正写入数据库了。"单测我们只测功能，不要对数据造成污染"
public class UserServiceImplTest extends MallApplicationTests {

    private static final String USERNAME = "jack";
    private static final String PASSWORD = "123456";
    private static final String EMAIL = "jack@qq.com";

    @Autowired
    private IUserService userService;

    @Test
//  @Before//注解可以使得下面的其他测试方法运行前都先执行一遍register方法注册一个用户，就不需要其他测试方法显式调用register
    public void register() {
        User user = new User(USERNAME,PASSWORD,EMAIL, RoleEnum.CUSTOMER.getCode());
        userService.register(user);
    }

    @Test
    public void login(){
        register();//先注册
        ResponseVo<User> responseVo = userService.login(USERNAME, PASSWORD);
        //assert断言，判断status的结果是否和预期的一样，如果一样就正常执行，否则会抛出AssertionError。
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
}