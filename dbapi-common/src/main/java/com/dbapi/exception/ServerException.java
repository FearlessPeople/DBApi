package com.dbapi.exception;

/**
 * 通用服务端异常类
 */

public class ServerException extends RuntimeException {

    private int code;  // 错误码

    public ServerException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServerException(String message) {
        super(message);
        this.code = 500;  // 默认错误码
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
