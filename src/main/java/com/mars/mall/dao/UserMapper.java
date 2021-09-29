package com.mars.mall.dao;

import com.mars.mall.pojo.User;

/**
 * 用户模块dao层
 */
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //以下两个方法是后面加的，用于注册用户时的信息校验
    int countByUsername(String username);//返回值大于零则证明数据库已存在相同的username

    int countByEmail(String email);//返回值大于零则证明数据库已存在相同的email
}