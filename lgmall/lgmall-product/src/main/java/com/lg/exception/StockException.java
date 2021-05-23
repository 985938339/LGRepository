package com.lg.exception;

public class StockException extends RuntimeException {


    public StockException(String message) {
        super(message);
    }

    public StockException(Throwable cause) {
        super(cause);
    }

    public StockException(String message, Throwable cause) {
        super(message, cause);
    }
}
