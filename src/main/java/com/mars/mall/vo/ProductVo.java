package com.mars.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Mars
 * @create: 2021-10-02 00:36
 **/
@Data
public class ProductVo {

    private Integer id;//商品id

    private Integer categoryId;//分类id,对应mall_category表的主键

    private String name;//商品名称

    private String subtitle;//产品副标题

    private String mainImage;//产品主图,url相对地址

    private BigDecimal price;//价格,单位-元保留两位小数

    private Integer status;//商品状态.1-在售 2-下架 3-删除
}
