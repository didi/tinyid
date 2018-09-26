package com.xiaoju.uemc.tinyid.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author du_imba
 */
@EnableAsync
@SpringBootApplication
@EnableScheduling
public class TinyIdServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinyIdServerApplication.class, args);
    }
}
