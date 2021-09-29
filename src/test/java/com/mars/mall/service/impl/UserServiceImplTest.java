package com.mars.mall.service.impl;

import com.mars.mall.MallApplicationTests;
import com.mars.mall.enums.RoleEnum;
import com.mars.mall.pojo.User;
import com.mars.mall.service.IUserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Transactional //在测试类中加入这个事务注解，可以回滚，这样所测试的数据就不会被真正写入数据库了。"单测我们只测功能，不要对数据造成污染"
public class UserServiceImplTest extends MallApplicationTests {

    @Autowired
    private IUserService userService;

    @Test
    public void register() {
        User user = new User("jack","123456","jack@qq.com", RoleEnum.CUSTOMER.getCode());

        userService.register(user);
    }
}