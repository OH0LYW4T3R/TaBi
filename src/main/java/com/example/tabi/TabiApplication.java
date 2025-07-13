package com.example.tabi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// CI/CD
@SpringBootApplication
@EnableScheduling
public class TabiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TabiApplication.class, args);
    }

}
