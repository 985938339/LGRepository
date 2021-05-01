package com.lg.exception;

import com.baomidou.mybatisplus.extension.api.IErrorCode;

public class ApiException extends Exception {


    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
