package com.kdanmobile.demoJava.exception;

import lombok.Getter;

/**
 * 自定义异常
 *
 * @author ZhouQiang 2022/7/5
 */
@Getter
public class CommonException extends RuntimeException {

    private final String code;


    public CommonException(String code, String msg) {
        super(msg);
        this.code = code;
    }




}
