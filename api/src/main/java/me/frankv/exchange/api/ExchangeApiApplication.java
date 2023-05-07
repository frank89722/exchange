package me.frankv.exchange.api;

import me.frankv.exchange.api.feign.CoreService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ExchangeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeApiApplication.class, args);
	}

}
