package com.mars.mall.service.impl;

import com.mars.mall.dao.UserMapper;
import com.mars.mall.enums.ResponseEnum;
import com.mars.mall.enums.RoleEnum;
import com.mars.mall.pojo.User;
import com.mars.mall.service.IUserService;
import com.mars.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;


/**
 * @description: 用户模块Service层：调用用户模块Dao层接口实现注册、登录、获取用户信息、退出登录功能
 * @author: Mars
 * @create: 2021-09-26 23:13
 **/
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;//用户模块dao实例

    /**
     * 注册功能的实现
     * @param user 传入的user有username,password,email,role属性
     */
    @Override
    public ResponseVo<User> register(User user) {

        //校验传入信息是否合法
        //username不能重复
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if (countByUsername > 0){ //数据库已存在同样username的用户
            return ResponseVo.error(ResponseEnum.USERNAME_EXIST);
        }
        //email不能重复
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if (countByEmail > 0){ //数据库已存在同样email的用户
            return ResponseVo.error(ResponseEnum.EMAIL_EXIST);
        }

        //设置用户角色，默认是客户(状态码是1)
        user.setRole(RoleEnum.CUSTOMER.getCode());

        //用MD5给密码加密，MD5摘要算法(Spring自带)：使得保存在数据库中的用户密码是加密过的，提高安全性
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));

        //将信息写入数据库
        int resultCount = userMapper.insertSelective(user);
        if (resultCount == 0){ //如果返回值是0，则意味着写入数据库时出错，服务端错误
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        return ResponseVo.success();//若上述都没出错，则返回成功
    }

    /**
     * 登录功能的实现
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public ResponseVo<User> login(String username, String password) {
        User user = userMapper.selectByUsername(username);//根据给定用户名在数据库中查找这个user
        if (user == null){
            //用户不存在(返回:用户名或密码错误)
            return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        if (!user.getPassword().equalsIgnoreCase(
                DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)))) {
            //密码错误(返回:用户名或密码错误)
            return ResponseVo.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        //程序如果能运行到这，证明数据库确实有此用户且密码正确
        user.setPassword("");//先将这个生成的用户对象密码置空串，防止将数据库中存储的用户密码存进了Session中
        return ResponseVo.success(user); //这里传入user用于之后在controller中设置session
    }
}
