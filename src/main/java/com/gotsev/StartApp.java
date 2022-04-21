package com.gotsev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class StartApp {

    public static void main(String... args) {
        SpringApplication.run(StartApp.class, args);
    }
}
