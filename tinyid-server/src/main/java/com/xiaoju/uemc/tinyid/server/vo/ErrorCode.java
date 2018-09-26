package com.xiaoju.uemc.tinyid.server.vo;

/**
 * @author du_imba
 */
public enum ErrorCode {
    /**
     * token is wrong
     */
    TOKEN_ERR(5, "token is error"),
    /**
     * server internal error
     */
    SYS_ERR(6, "sys error");

    private Integer code;
    private String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }

}


