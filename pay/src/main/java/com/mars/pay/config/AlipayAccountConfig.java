package com.mars.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Mars
 * @ConfigurationProperties(prefix = "xxx") 解析：
 * 当获取主配置文件中属性值时，只需@ConfigurationProperties(prefix = "xxx")注解来修饰某类，
 * 其作用是告诉springBoot，此类中的属性将与默认的全局配置文件中对应属性一一绑定。属性名必须是application.yml
 * 或application.properties。【prefix = "xxx"】表示与配置文件中哪个层级(xxx)的属性进行绑定。
 */
@Component
@ConfigurationProperties(prefix = "alipay")
@Data
public class AlipayAccountConfig {

	private String appId;

	private String privateKey;

	private String publicKey;

	private String notifyUrl;

	private String returnUrl;
}
