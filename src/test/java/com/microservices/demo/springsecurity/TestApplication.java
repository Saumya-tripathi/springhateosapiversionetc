package com.microservices.demo.springsecurity;

import org.springframework.boot.SpringApplication;

public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpringsecurityApplication::main) // Replace with your actual main class
                .with(TestContainersConfig.class)
                .run(args);
    }
}