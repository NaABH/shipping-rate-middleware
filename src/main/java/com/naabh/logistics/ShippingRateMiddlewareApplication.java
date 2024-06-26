package com.naabh.logistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication (exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
public class ShippingRateMiddlewareApplication {

	private static final Logger logger = LoggerFactory.getLogger(ShippingRateMiddlewareApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ShippingRateMiddlewareApplication.class, args);
		logger.info("Shipping Rate Middleware Application Started");
	}

}
