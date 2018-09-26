package com.xiaoju.uemc.tinyid.server.vo;

/**
 * @author du_imba
 */
public class Response<T> {
    private T data;
    private Integer code = 200;
    private String message = "";

    public static final int SYS_ERROR = 500;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
