package com.lg.exception;

public class TimeOutException extends Exception {


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
