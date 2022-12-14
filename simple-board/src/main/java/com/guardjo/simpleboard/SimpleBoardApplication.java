package com.guardjo.simpleboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class SimpleBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleBoardApplication.class, args);
    }

}
