package org.fd.ase.grp15.ase_contribute_service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class AseContributeServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(AseContributeServiceApplication.class, args);
    }
}
