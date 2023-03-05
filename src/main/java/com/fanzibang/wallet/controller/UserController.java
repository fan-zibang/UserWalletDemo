package com.fanzibang.wallet.controller;

import com.fanzibang.wallet.dto.PayDTO;
import com.fanzibang.wallet.common.CommonResult;
import com.fanzibang.wallet.dto.RefundDTO;
import com.fanzibang.wallet.service.AccountDetailsService;
import com.fanzibang.wallet.service.UserService;
import com.fanzibang.wallet.vo.AccountDetailsVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private AccountDetailsService accountDetailsService;

    @GetMapping("/balance")
    public CommonResult getUserBalance() {
        // 获取当前登录用户的id
        // 用户登录成功后可以携带token，拦截器拦截验证后，将用户信息存到ThreadLocal中
        // 这里省略具体实现  UserHolder.get().getUserId()
        // 模拟用户
        Long userId = 1L;
        BigDecimal decimal = userService.getUserBalance(userId);
        return CommonResult.success(decimal);
    }

    @GetMapping("/balance/balanceDetails")
    public CommonResult getUserBalanceDetails() {
        // 获取当前登录用户的id
        // 用户登录成功后可以携带token，拦截器拦截验证后，将用户信息存到ThreadLocal中
        // 这里省略具体实现  UserHolder.get().getUserId()
        // 模拟用户
        Long userId = 1L;
        List<AccountDetailsVO> accountDetailsList = accountDetailsService.getUserBalanceDetails(userId);
        return CommonResult.success(accountDetailsList);
    }

    @PostMapping("/balance/pay")
    public CommonResult pay(@RequestBody PayDTO payDTO) {
        Boolean success = userService.pay(payDTO);
        if (success) {
            return CommonResult.success("支付成功");
        }
        return CommonResult.fail("支付失败");
    }

    @PostMapping("/balance/refund")
    public CommonResult refund(@RequestBody RefundDTO refundDTO) {
        Boolean success = userService.refund(refundDTO);
        if (success) {
            return CommonResult.success("退款成功");
        }
        return CommonResult.fail("退款失败");
    }
}
