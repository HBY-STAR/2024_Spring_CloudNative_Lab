package org.fd.ase.grp15.ase_file_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;

@SpringBootApplication
public class AseFileServiceApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(AseFileServiceApplication.class, args);
    }
}