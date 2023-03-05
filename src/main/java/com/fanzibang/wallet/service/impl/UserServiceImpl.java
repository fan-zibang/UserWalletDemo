package com.fanzibang.wallet.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.fanzibang.wallet.dto.PayDTO;
import com.fanzibang.wallet.common.AccountDetailsConstant;
import com.fanzibang.wallet.domain.AccountDetails;
import com.fanzibang.wallet.dto.RefundDTO;
import com.fanzibang.wallet.mapper.UserMapper;
import com.fanzibang.wallet.service.AccountDetailsService;
import com.fanzibang.wallet.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountDetailsService accountDetailsService;

    /**
     * 获取用户账户余额
     * @param userId 用户id
     * @return
     */
    @Override
    public BigDecimal getUserBalance(Long userId) {
        BigDecimal balance = userMapper.getUserBalance(userId);
        Optional.ofNullable(balance).orElse(BigDecimal.ZERO);
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            log.error("用户{}余额为负数", userId);
            balance = BigDecimal.ZERO;
        }
        return balance;
    }

    /**
     * 消费账户余额
     * @param payDTO 支付对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean pay(PayDTO payDTO) {
        // 是否重复下单
        // 判断订单状态 未支付 支付成功 支付失败 退款中 退款完成
        // 判断订单支付状态 未支付 支付成功 支付取消 支付超时关闭
        // 以上判断在订单服务完成
        Long userId = payDTO.getUserId();
        BigDecimal totalAmount = payDTO.getTotalAmount();
        BigDecimal balance = null;
        synchronized (userId.toString().intern()) {
            // 判断余额是否充足
            balance = this.getUserBalance(userId);
            if (balance.compareTo(totalAmount) < 0) {
                throw new RuntimeException("余额不足");
            }
            // 余额充足，扣减用户余额
            int i = userMapper.spend(userId, totalAmount);
            if (i < 0) {
                throw new RuntimeException("扣减失败");
            }
        }
        // 添加一条明细
        AccountDetails accountDetails = new AccountDetails();
        // 参数1为终端ID 参数2为数据中心ID
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        accountDetails.setAccountNumber(snowflake.nextId());
        accountDetails.setUserId(userId);
        accountDetails.setAccountType(AccountDetailsConstant.ACCOUNT_TYPE_OUTCOME);
        accountDetails.setAccount(totalAmount);
        accountDetails.setBalance(balance.subtract(totalAmount));
        accountDetails.setRemark(payDTO.getSubject());
        accountDetails.setCreateTime(DateUtil.now());
        int i = accountDetailsService.insert(accountDetails);
        if (i < 0) {
            throw new RuntimeException("明细新增失败");
        }
        return true;
    }

    /**
     * 退款
     * @param refundDTO 退款实体
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean refund(RefundDTO refundDTO) {
        // 判断订单状态和订单支付状态 略
        Long userId = refundDTO.getUserId();
        BigDecimal balance = this.getUserBalance(userId);
        BigDecimal refundAmount = refundDTO.getRefundAmount();
        synchronized (userId.toString().intern()) {
            // 更新用户余额
            int i = userMapper.refund(userId, refundAmount);
            if (i < 0) {
                throw new RuntimeException("更新用户余额失败");
            }
        }
        // 添加一条明细
        AccountDetails accountDetails = new AccountDetails();
        // 参数1为终端ID 参数2为数据中心ID
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        accountDetails.setAccountNumber(snowflake.nextId());
        accountDetails.setUserId(userId);
        accountDetails.setAccountType(AccountDetailsConstant.ACCOUNT_TYPE_INCOME);
        accountDetails.setAccount(refundAmount);
        accountDetails.setBalance(balance.add(refundAmount));
        accountDetails.setRemark("退款");
        accountDetails.setCreateTime(DateUtil.now());
        int i = accountDetailsService.insert(accountDetails);
        if (i < 0) {
            throw new RuntimeException("明细新增失败");
        }
        return true;
    }
}
