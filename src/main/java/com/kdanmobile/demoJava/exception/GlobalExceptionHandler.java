package com.kdanmobile.demoJava.exception;

import com.kdanmobile.demoJava.base.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ZhouQiang 2022/7/5
 */
@RestControllerAdvice(basePackages = "com.kdanmobile.demoJava")
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public R<Void> handlerThrowable(Throwable t) {
        log.info("错误类：{}", t.getClass());
        log.error(t.getMessage(), t);
        return R.error("500","未知异常");
    }

    @ExceptionHandler(CommonException.class)
    public R<Void> handlerThrowable(CommonException e) {
        log.error(e.getMessage(), e);
        return R.error(e.getCode(), e.getMessage());
    }

}
