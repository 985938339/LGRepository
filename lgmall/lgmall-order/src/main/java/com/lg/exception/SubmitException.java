package com.lg.exception;

public class SubmitException extends RuntimeException {


    public SubmitException(String message) {
        super(message);
    }

    public SubmitException(Throwable cause) {
        super(cause);
    }

    public SubmitException(String message, Throwable cause) {
        super(message, cause);
    }
}
