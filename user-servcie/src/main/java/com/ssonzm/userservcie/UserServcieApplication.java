package com.ssonzm.userservcie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UserServcieApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServcieApplication.class, args);
    }

}
