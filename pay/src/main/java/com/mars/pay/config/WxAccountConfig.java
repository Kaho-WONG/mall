package com.mars.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Mars
 */
@Component
@ConfigurationProperties(prefix = "wx")
@Data
public class WxAccountConfig {

	private String appId;

	private String mchId;

	private String mchKey;

	private String notifyUrl;

	private String returnUrl;
}
