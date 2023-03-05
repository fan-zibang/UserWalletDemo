package com.fanzibang.wallet.service;

import com.fanzibang.wallet.dto.PayDTO;
import com.fanzibang.wallet.dto.RefundDTO;

import java.math.BigDecimal;

public interface UserService {

    BigDecimal getUserBalance(Long userId);

    Boolean pay(PayDTO payDTO);

    Boolean refund(RefundDTO refundDTO);

}
