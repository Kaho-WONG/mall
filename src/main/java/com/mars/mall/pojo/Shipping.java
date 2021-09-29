package com.mars.mall.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 收货地址类 —— 收货地址表
 */
@Data
public class Shipping {
    private Integer id;

    private Integer userId;//用户id

    private String receiverName;//收件人姓名

    private String receiverPhone;//收件人固定电话号码

    private String receiverMobile;//收件人移动电话号码

    private String receiverProvince;//省份

    private String receiverCity;//城市

    private String receiverDistrict;//区/县

    private String receiverAddress;//详细地址

    private String receiverZip;//邮编

    private Date createTime;//创建时间

    private Date updateTime;//更新时间

}