package com.mars.mall.dao;

import com.mars.mall.pojo.User;

/**
 * 用户模块dao层
 */
public interface UserMapper {

    int deleteByPrimaryKey(Integer id);//根据用户id删除用户

    /*
    insertSelective对应的sql语句加入了NULL校验，即只会插入数据不为null的字段值。
    insert则会插入所有字段，会插入null。
     */
    int insert(User record);//插入一个用户记录

    int insertSelective(User record);//插入一个用户记录

    User selectByPrimaryKey(Integer id);//根据用户id查找出一个User用户

    int updateByPrimaryKeySelective(User record);//根据传入user的主键id更新用户信息，带判空

    int updateByPrimaryKey(User record);//根据传入user的主键id更新用户信息，不带判空

    //用于注册用户时的信息校验
    int countByUsername(String username);//返回值大于零则证明数据库已存在相同的username

    int countByEmail(String email);//返回值大于零则证明数据库已存在相同的email

    //用于登录用户时的信息校验
    User selectByUsername(String username);//根据用户名查询用户
}