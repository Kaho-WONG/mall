package com.mars.pay.service;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import com.mars.pay.pojo.PayInfo;

import java.math.BigDecimal;

/**
 * @description:
 * @author: Mars
 * @create: 2021-09-26 14:02
 **/
public interface IPayService {

    /**
     * 创建/发起支付
     */
    PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum);

    /**
     * 异步通知处理
     * @param notifyData
     */
    String asyncNotify(String notifyData);

    /**
     * 通过订单号查询订单信息
     * @param orderId
     * @return
     */
    PayInfo queryByOrderId(String orderId);
}
