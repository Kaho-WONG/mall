package com.mars.mall.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品类 —— 商品表
 */
@Data
public class Product {
    private Integer id;//商品id

    private Integer categoryId;//分类id,对应mall_category表的主键

    private String name;//商品名称

    private String subtitle;//产品副标题

    private String mainImage;//产品主图,url相对地址

    private String subImages;//图片地址,json格式,扩展用

    private String detail;//商品详情

    private BigDecimal price;//价格,单位-元保留两位小数

    private Integer stock;//库存数量

    private Integer status;//商品状态.1-在售 2-下架 3-删除

    private Date createTime;//创建时间

    private Date updateTime;//更新时间

}