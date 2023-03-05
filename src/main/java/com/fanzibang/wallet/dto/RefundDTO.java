package com.fanzibang.wallet.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefundDTO {

    /**
     * 退款人id
     */
    private Long userId;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 消费总金额 单位为元，精确到小数点后两位，取值范围为 [0.01,100000000]。金额不能为0
     */
    private BigDecimal refundAmount;
}
