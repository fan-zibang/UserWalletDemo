package com.fanzibang.wallet.mapper;

import com.fanzibang.wallet.domain.AccountDetails;
import com.fanzibang.wallet.vo.AccountDetailsVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountDetailsMapper {

    List<AccountDetails> selectBalanceDetailsList(Long userId);

    int insert(AccountDetails accountDetails);

}




