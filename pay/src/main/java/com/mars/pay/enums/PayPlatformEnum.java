package com.mars.pay.enums;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Getter;

/**
 * Created by Mars
 */
@Getter
public enum PayPlatformEnum {

	//1-支付宝,2-微信
	ALIPAY(1),

	WX(2),
	;

	Integer code;

	PayPlatformEnum(Integer code) {
		this.code = code;
	}

	public static PayPlatformEnum getByBestPayTypeEnum(BestPayTypeEnum bestPayTypeEnum) {
		//直接用一个for遍历已有的枚举支付方式
		for (PayPlatformEnum payPlatformEnum : PayPlatformEnum.values()) {
			if (bestPayTypeEnum.getPlatform().name().equals(payPlatformEnum.name())) {
				return payPlatformEnum;
			}
		}
		throw new RuntimeException("错误的支付平台: " + bestPayTypeEnum.name());
	}
}
