package org.fd.ase.grp15.ase_notification_service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class AseNotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AseNotificationServiceApplication.class, args);
	}

}
