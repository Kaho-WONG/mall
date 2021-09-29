package com.mars.mall.dao;

import com.mars.mall.pojo.Category;

import java.util.List;

/**
 * 类别模块dao层
 */
public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> selectAll(); //一次性查询所有类目记录
}