package com.lg.exception;


public class PriceException extends RuntimeException {

    public PriceException(String message) {
        super(message);
    }

    public PriceException(Throwable cause) {
        super(cause);
    }

    public PriceException(String message, Throwable cause) {
        super(message, cause);
    }
}