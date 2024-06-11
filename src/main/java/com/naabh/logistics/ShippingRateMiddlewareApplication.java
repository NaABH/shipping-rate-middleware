package com.naabh.logistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShippingRateMiddlewareApplication {

	private static final Logger logger = LoggerFactory.getLogger(ShippingRateMiddlewareApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ShippingRateMiddlewareApplication.class, args);
		logger.info("Shipping Rate Middleware Application Started");
	}

}
