package com.lg.exception;

public class TimeOutException extends RuntimeException {


    public TimeOutException(String message) {
        super(message);
    }

    public TimeOutException(Throwable cause) {
        super(cause);
    }

    public TimeOutException(String message, Throwable cause) {
        super(message, cause);
    }
}
