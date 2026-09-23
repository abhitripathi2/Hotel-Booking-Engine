package com.codingShuttle.projects.HotelManagement.App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HotelBookingEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelBookingEngineApplication.class, args);
	}

}
