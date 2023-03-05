package com.fanzibang.wallet;

import com.fanzibang.wallet.service.UserService;
import com.fanzibang.wallet.utils.SignUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;

@SpringBootTest
class UserWalletDemoApplicationTests {

    // 盐 只在本地端和客户端存储 不在网络上传递
    private static final String salt = "abc";

    @Resource
    private UserService userService;

    /**
     * 测试查询用户钱包余额
     */
    @Test
    void testGetUserBalance() {
        BigDecimal balance = userService.getUserBalance(1L);
        System.out.println(balance);
    }

    /**
     * 测试签名工具
     */
    @Test
    void testSignUtils() {
        String sign1 = SignUtils.sign("{\"orderNumber\":1231241234,\"subject\":\"转账\",\"totalAmount\":10,\"userId\":1}" + salt);
        String sign2 = SignUtils.sign("{\"orderNumber\":12312312123,\"refundAmount\":10.00,\"userId\":1}" + salt);
        System.out.println(sign1);
        System.out.println(sign2);
    }


}
