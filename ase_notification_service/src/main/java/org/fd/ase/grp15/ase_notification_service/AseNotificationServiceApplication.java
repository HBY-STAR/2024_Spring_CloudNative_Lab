package org.fd.ase.grp15.ase_notification_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AseNotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AseNotificationServiceApplication.class, args);
	}

}
