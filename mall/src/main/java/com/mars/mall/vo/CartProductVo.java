package com.mars.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 购物车内的一样商品
 * @author: Mars
 * @create: 2021-10-02 15:28
 **/
@Data
public class CartProductVo {

    private Integer productId;//商品id

    private Integer quantity;//商品购买数量

    private String productName;//商品名称

    private String productSubtitle;//商品子标题

    private String productMainImage;//主图

    private BigDecimal productPrice;//商品单价

    private Integer productStatus;//商品状态

    /**
     * 等于 quantity * productPrice
     */
    private BigDecimal productTotalPrice;//这件商品总价

    private Integer productStock;//商品库存

    private Boolean productSelected;//商品是否选中

    public CartProductVo(Integer productId, Integer quantity, String productName,
                         String productSubtitle, String productMainImage,
                         BigDecimal productPrice, Integer productStatus,
                         BigDecimal productTotalPrice, Integer productStock,
                         Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productSubtitle = productSubtitle;
        this.productMainImage = productMainImage;
        this.productPrice = productPrice;
        this.productStatus = productStatus;
        this.productTotalPrice = productTotalPrice;
        this.productStock = productStock;
        this.productSelected = productSelected;
    }
}
