package com.fanzibang.wallet.config;

import com.fanzibang.wallet.filter.SignFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SignFilterConfiguration {

    @Value("${sign.expiredTime}")
    private String signExpiredTime;

    // filter中的初始化参数
    private Map<String, String> initParametersMap = new HashMap<>();

    @Bean
    public FilterRegistrationBean contextFilterRegistrationBean() {
        initParametersMap.put("signExpiredTime", signExpiredTime);
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(signFilter());
        registration.setInitParameters(initParametersMap);
        // 对需要签名认证的路径拦截
        registration.addUrlPatterns("/user/balance/pay", "/user/balance/refund");
        registration.setName("SignFilter");
        // 设置过滤器被调用的顺序
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public Filter signFilter() {
        return new SignFilter();
    }
}
