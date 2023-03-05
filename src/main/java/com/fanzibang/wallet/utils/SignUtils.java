package com.fanzibang.wallet.utils;

import org.springframework.util.DigestUtils;

public class SignUtils {

    // 盐 只在本地端和客户端存储 不在网络上传递
    private static final String salt = "abc";

    public static String sign(String s) {
        return DigestUtils.md5DigestAsHex(s.getBytes());
    }

    public static Boolean verifySign(String s, String sign) {
        return sign.equals(sign(s + salt));
    }
}
