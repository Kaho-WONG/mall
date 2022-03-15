package com.mars.pay.controller;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import com.mars.pay.pojo.PayInfo;
import com.mars.pay.service.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: Mars
 * @create: 2021-09-26 20:17
 **/
@Controller
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    IPayService payService;

    @Autowired
    private WxPayConfig wxPayConfig;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("amount") BigDecimal amount,
                               @RequestParam("payType")BestPayTypeEnum bestPayTypeEnum){

        if (bestPayTypeEnum != BestPayTypeEnum.WXPAY_NATIVE || bestPayTypeEnum != BestPayTypeEnum.ALIPAY_PC) {
            throw new RuntimeException("暂不支持的支付类型");
        }
        //缺少微信商户密钥，没办法顺利调用这个方法返回response，所以先注掉了
//        PayResponse response = payService.create(orderId, amount, bestPayTypeEnum);
        //同理，这里由于我缺少微信商户密钥，没办法顺利生成返回的codeUrl，所以写死了一个二维码
//        map.put("codeUrl",response.getCodeUrl());
        //支付方式不同，渲染就不同, WXPAY_NATIVE使用codeUrl,  ALIPAY_PC使用body
        Map<String, String> map = new HashMap<>();
//        if (bestPayTypeEnum == BestPayTypeEnum.WXPAY_NATIVE) {
//            map.put("codeUrl", response.getCodeUrl());
//            map.put("orderId", orderId);
//            map.put("returnUrl", wxPayConfig.getReturnUrl());
//            return new ModelAndView("createForWxNative", map);
//        }else if (bestPayTypeEnum == BestPayTypeEnum.ALIPAY_PC) {
//            map.put("body", response.getBody());
//            return new ModelAndView("createForAlipayPc", map);
//        }

        map.put("codeUrl","https://static01.imgkr.com/temp/d731c13ec85d4091971dfc596ef1d6d7.png");
        return new ModelAndView("create",map);
    }

    @PostMapping("/notify")
    @ResponseBody
    public String asyncNotify(@RequestBody String notifyData){
        return payService.asyncNotify(notifyData);
    }

    @GetMapping("/queryByOrderId")
    @ResponseBody
    public PayInfo queryByOrderId(@RequestParam String orderId) {
        log.info("查询支付记录...");
        return payService.queryByOrderId(orderId);
    }
}
