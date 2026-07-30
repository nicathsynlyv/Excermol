package com.example.Excermol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // kohne qeydleri silmek ucun istifade olunan annotasiya
public class ExcermolApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExcermolApplication.class, args);
	}

}
