package com.pacal.share;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.pacal.share"})
public class FrontRestApplication {

    public static void main(String[] args) {
        SpringApplication.run( FrontRestApplication.class, args );
    }
}