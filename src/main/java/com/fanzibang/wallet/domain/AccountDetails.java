package com.fanzibang.wallet.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName tb_account_details
 */
@Data
public class AccountDetails implements Serializable {
    /**
     * 流水号
     */
    private Long accountNumber;

    /**
     * 对应用户id
     */
    private Long userId;

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
    private Integer accountType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作时间
     */
    private String createTime;

    private static final long serialVersionUID = 1L;
}