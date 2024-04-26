package com.immoben.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan({ "com.immoben.common.entity", "com.immoben.common.entity.product" })
public class ImmobenBackEndApplication {

	public static void main (String[] args) {
		SpringApplication.run(ImmobenBackEndApplication.class, args);
	}

}
