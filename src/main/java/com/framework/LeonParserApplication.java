package com.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LeonParserApplication {

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(LeonParserApplication.class, args)));
    }
}