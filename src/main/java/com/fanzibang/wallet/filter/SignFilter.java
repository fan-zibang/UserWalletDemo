package com.fanzibang.wallet.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.fanzibang.wallet.common.CommonResult;
import com.fanzibang.wallet.component.CachedBodyHttpServletRequest;
import com.fanzibang.wallet.utils.SignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StreamUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

public class SignFilter implements Filter {

    private Long signExpiredTime;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequestWrapper requestWrapper = new CachedBodyHttpServletRequest(request);

        // 时间戳
        Long timestamp = Long.valueOf(request.getHeader("timestamp"));
        // 随机值
        String nonce = request.getHeader("nonce");
        // sign
        String sign = request.getHeader("sign");
        if (Objects.isNull(timestamp) || StrUtil.isEmpty(nonce) || StrUtil.isEmpty(sign)) {
            this.writeJson(response, CommonResult.fail("reject 1"));
            return;
        }

        /*
         * 1.重放验证
         * 判断timestamp与当前时间是否超过60s，超过了就提示签名过期
         */
        long now = System.currentTimeMillis() / 1000;
        if (now - timestamp > signExpiredTime) {
            this.writeJson(response, CommonResult.fail("reject 2"));
            return;
        }

        /**
         * 2.验证nonce
         * 判断redis是否存在这个key，存在说明调用过一次，不存在存入redis，并设置过期时间为signExpiredTime
         */
        Boolean exist = stringRedisTemplate.opsForValue().setIfAbsent("nonce:" + nonce, "1", signExpiredTime, TimeUnit.SECONDS);
        if (!exist) {
            this.writeJson(response, CommonResult.fail("reject 3"));
            return;
        }

        /**
         * 3.验签
         */
        byte[] requestBody = StreamUtils.copyToByteArray(requestWrapper.getInputStream());
        SortedMap sortedMap = JSON.parseObject(new String(requestBody), SortedMap.class);
        String paramsJson = JSON.toJSONString(sortedMap);
        if (!SignUtils.verifySign(paramsJson, sign)) {
            this.writeJson(response, CommonResult.fail("reject 4"));
            return;
        }

        filterChain.doFilter(requestWrapper, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.signExpiredTime = Long.valueOf(filterConfig.getInitParameter("signExpiredTime"));
    }

    private void writeJson(HttpServletResponse response, CommonResult commonResult) {
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(JSON.toJSONString(commonResult));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
