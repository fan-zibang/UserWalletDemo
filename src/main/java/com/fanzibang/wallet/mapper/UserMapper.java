package com.fanzibang.wallet.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface UserMapper {

    BigDecimal getUserBalance(Long userId);

    int spend(@Param("userId") Long userId, @Param("totalAmount") BigDecimal totalAmount);

    int refund(@Param("userId") Long userId, @Param("refundAmount") BigDecimal refundAmount);

}




