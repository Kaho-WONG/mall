package com.mars.mall.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单条目类 —— 订单条目表
 */
@Data
public class OrderItem {
    private Integer id;//订单子表id

    private Integer userId;//订单所属用户id

    private Long orderNo;//订单号

    private Integer productId;//商品id

    private String productName;//商品名称

    private String productImage;//商品图片地址

    private BigDecimal currentUnitPrice;//生成订单时的商品单价，单位是元,保留两位小数'

    private Integer quantity;//商品数量

    private BigDecimal totalPrice;//商品总价,单位是元,保留两位小数

    private Date createTime;//创建时间

    private Date updateTime;//更新时间

}