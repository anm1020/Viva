package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.demo.model.entity")
public class vivaApplication {

    public static void main(String[] args) {
        SpringApplication.run(vivaApplication.class, args);
    }

}
