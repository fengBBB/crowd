package com.feng.crowd.exception;

/*
* 添加新用户时 用户名重复了抛出的异常
* */
public class LogginAcctAlreadyInUseException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public LogginAcctAlreadyInUseException() {
        super();
    }

    public LogginAcctAlreadyInUseException(String message) {
        super(message);
    }

    public LogginAcctAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogginAcctAlreadyInUseException(Throwable cause) {
        super(cause);
    }

    public LogginAcctAlreadyInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
