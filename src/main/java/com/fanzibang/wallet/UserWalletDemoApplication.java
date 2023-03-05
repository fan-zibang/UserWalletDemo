package com.fanzibang.wallet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"com.fanzibang.wallet.mapper"})
@SpringBootApplication(scanBasePackages = {"com.fanzibang.wallet"})
public class UserWalletDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserWalletDemoApplication.class, args);
    }

}
