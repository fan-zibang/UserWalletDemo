package com.fanzibang.wallet.common;

import lombok.Getter;

public enum ReturnCode {
    /** 操作成功 **/
    RC100(100,"success"),

    /** 操作失败 **/
    RC999(999,"fail");

    // 自定义状态码
    @Getter
    private final int code;
    // 自定义描述
    @Getter
    private final String message;

    ReturnCode(int code, String message){
        this.code = code;
        this.message = message;
    }
}
