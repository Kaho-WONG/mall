package com.mars.mall.dao;

import com.mars.mall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 商品模块dao层
 */
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    //传入参数不是基础类型，得用注解标识并在mapper配置文件中把collection指定
    List<Product> selectByCategoryIdSet(@Param("categoryIdSet") Set<Integer> categoryIdSet);//通过类目id集合查找商品

    List<Product> selectByProductIdSet(@Param("productIdSet") Set<Integer> productIdSet);//通过商品id集合查找商品
}