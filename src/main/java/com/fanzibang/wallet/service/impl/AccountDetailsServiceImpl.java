package com.fanzibang.wallet.service.impl;

import com.fanzibang.wallet.domain.AccountDetails;
import com.fanzibang.wallet.mapper.AccountDetailsMapper;
import com.fanzibang.wallet.service.AccountDetailsService;
import com.fanzibang.wallet.vo.AccountDetailsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountDetailsServiceImpl implements AccountDetailsService {

    @Autowired
    private AccountDetailsMapper accountDetailsMapper;

    /**
     * 获取用户账户明细
     * @param userId 用户id
     * @return
     */
    @Override
    public List<AccountDetailsVO> getUserBalanceDetails(Long userId) {
        List<AccountDetailsVO> collect = accountDetailsMapper.selectBalanceDetailsList(userId)
                .stream().map(accountDetails -> {
                    AccountDetailsVO accountDetailsVO = new AccountDetailsVO();
                    BeanUtils.copyProperties(accountDetails, accountDetailsVO);
                    String type = accountDetails.getAccountType() == 1 ? "收入" : "支出";
                    accountDetailsVO.setAccountType(type);
                    return accountDetailsVO;
                }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public int insert(AccountDetails accountDetails) {
        return accountDetailsMapper.insert(accountDetails);
    }
}
