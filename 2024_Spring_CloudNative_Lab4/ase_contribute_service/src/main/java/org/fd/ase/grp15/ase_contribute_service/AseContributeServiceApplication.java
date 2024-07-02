package org.fd.ase.grp15.ase_contribute_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AseContributeServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(AseContributeServiceApplication.class, args);
    }
}
