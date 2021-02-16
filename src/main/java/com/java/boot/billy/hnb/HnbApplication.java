package com.java.boot.billy.hnb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HnbApplication {

    public static void main(String[] args) {
        SpringApplication.run(HnbApplication.class, args);
    }

}
