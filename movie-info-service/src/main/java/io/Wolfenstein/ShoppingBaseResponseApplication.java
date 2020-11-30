package io.Wolfenstein;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import io.Wolfenstein.Util.EmailUtil;

@SpringBootApplication
@EnableEurekaClient
public class ShoppingBaseResponseApplication {
	@Bean
	public RestTemplate getRestTemplet() {
		HttpComponentsClientHttpRequestFactory clientHttpRequest=new HttpComponentsClientHttpRequestFactory();
		clientHttpRequest.setConnectTimeout(3000);
		return new RestTemplate();
	}
	@Bean
	public EmailUtil getEmailUtil() {
		return new EmailUtil();
	}

	public static void main(String[] args) {
		SpringApplication.run(ShoppingBaseResponseApplication.class, args);
	}

}
