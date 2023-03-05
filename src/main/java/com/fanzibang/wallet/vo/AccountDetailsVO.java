package com.fanzibang.wallet.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDetailsVO {

    /**
     * 流水号
     */
    private Long accountNumber;

    /**
     * 操作金额
     */
    private BigDecimal account;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 类型：1-收入 2-支出
     */
    private String accountType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作时间
     */
    private String createTime;

}
