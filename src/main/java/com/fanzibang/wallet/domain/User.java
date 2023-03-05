package com.fanzibang.wallet.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 
 * @TableName tb_user
 */
@Data
public class User implements Serializable {
    /**
     * 
     */
    private Long userId;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 账户余额
     */
    private BigDecimal accountBalance;

    private static final long serialVersionUID = 1L;
}