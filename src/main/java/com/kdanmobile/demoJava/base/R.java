package com.kdanmobile.demoJava.base;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 请求统一返回对象
 * @author ZhouQiang 2022/7/5
 */
@Data
@AllArgsConstructor
public class R<T> {

    private String code;
    private String msg;
    private T data;

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        return ok("success", data);
    }

    public static <T> R<T> ok(String msg, T data) {
        return ok("200", msg, data);
    }

    public static <T> R<T> ok(String code, String msg) {
        return new R<>(code, msg, null);
    }

    public static <T> R<T> ok(String code, String msg, T data) {
        return new R<>(code, msg, data);
    }


    public static <T> R<T> error(String code, String msg) {
        return error(code, msg, null);
    }

    public static <T> R<T> error(String code, String msg, T data) {
        return new R<>(code, msg, data);
    }

}
