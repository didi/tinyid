package com.xiaoju.uemc.tinyid.base.exception;

/**
 * @author zhangbingbing
 * @Description 系统时间回调异常
 * @date 2020/10/16
 */
public class SystemClockCallbackException extends RuntimeException {

    private static final long serialVersionUID = -6264588182225994225L;

    public SystemClockCallbackException(String msg) {
        super(msg);
    }

    public SystemClockCallbackException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
