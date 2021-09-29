package com.mars.mall.dao;

import com.mars.mall.MallApplicationTests;
import com.mars.mall.pojo.Category;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryMapperTest extends MallApplicationTests {

    @Autowired
    CategoryMapper categoryMapper;

    @Test
    public void deleteByPrimaryKey() {
    }

    @Test
    public void insert() {
    }

    @Test
    public void insertSelective() {
    }

    @Test
    public void selectByPrimaryKey() {
        Category category = categoryMapper.selectByPrimaryKey(100001);
        System.out.println(category.toString());
    }

    @Test
    public void updateByPrimaryKeySelective() {
    }

    @Test
    public void updateByPrimaryKey() {
    }
}