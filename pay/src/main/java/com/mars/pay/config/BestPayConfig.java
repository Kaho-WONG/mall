package com.mars.pay.config;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Mars
 * 统一配置BestPay的微信和支付宝接口信息，直接供service中调用(采用spring容器的自动注入)
 *
 * 支付的公私钥使用到了密码学的数字签名(非对称RSA签名)：数字签名的原理————>主要是一个签名和验签的过程
 * 一般发送方使用自己的私钥进行签名，接收方使用发送方的公钥进行验签，注意支付宝的的公私钥配置不能搞错！！！
 * 发起支付：商户(商户应用私钥签名) -> 支付宝(商户应用公钥验签)
 * 异步通知：支付宝(支付宝私钥签名) -> 商户(支付宝公钥验签)
 *
 */
@Component//标注一个类为Spring容器的Bean,(把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>)
public class BestPayConfig {

	@Autowired
	private WxAccountConfig wxAccountConfig;

	@Autowired
	private AlipayAccountConfig alipayAccountConfig;

	@Bean
	public BestPayService bestPayService(WxPayConfig wxPayConfig) {

		AliPayConfig aliPayConfig = new AliPayConfig();//支付宝支付配置
		aliPayConfig.setAppId(alipayAccountConfig.getAppId());
		//商户应用私钥
		aliPayConfig.setPrivateKey(alipayAccountConfig.getPrivateKey());
		//阿里支付宝公钥
		aliPayConfig.setAliPayPublicKey(alipayAccountConfig.getPublicKey());
		aliPayConfig.setNotifyUrl(alipayAccountConfig.getNotifyUrl());
		aliPayConfig.setReturnUrl(alipayAccountConfig.getReturnUrl());

		BestPayServiceImpl bestPayService = new BestPayServiceImpl();
		bestPayService.setWxPayConfig(wxPayConfig);
		bestPayService.setAliPayConfig(aliPayConfig);
		return bestPayService;
	}

	@Bean
	public WxPayConfig wxPayConfig() {
		WxPayConfig wxPayConfig = new WxPayConfig();//微信支付配置
		wxPayConfig.setAppId(wxAccountConfig.getAppId());//公众号appId
		wxPayConfig.setMchId(wxAccountConfig.getMchId());//商户号
		wxPayConfig.setMchKey(wxAccountConfig.getMchKey());//商户密钥
		//192.168.50.101 同一局域网可访问
		//125.121.56.227 云服务器可行，家庭宽带不行(路由器、光猫)
		wxPayConfig.setNotifyUrl(wxAccountConfig.getNotifyUrl());//接收支付平台异步通知的地址
		wxPayConfig.setReturnUrl(wxAccountConfig.getReturnUrl());
		return wxPayConfig;
	}
}
