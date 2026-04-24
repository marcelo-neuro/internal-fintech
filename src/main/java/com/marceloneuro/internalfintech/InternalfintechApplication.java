package com.marceloneuro.internalfintech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class InternalfintechApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternalfintechApplication.class, args);
	}

}
