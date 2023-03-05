package com.fanzibang.wallet.service;

import com.fanzibang.wallet.domain.AccountDetails;
import com.fanzibang.wallet.vo.AccountDetailsVO;

import java.util.List;

public interface AccountDetailsService {

    List<AccountDetailsVO> getUserBalanceDetails(Long userId);

    int insert(AccountDetails accountDetails);

}
