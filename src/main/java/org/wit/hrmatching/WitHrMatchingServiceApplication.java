package org.wit.hrmatching;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.wit.hrmatching.mapper")
public class WitHrMatchingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WitHrMatchingServiceApplication.class, args);
    }

}
