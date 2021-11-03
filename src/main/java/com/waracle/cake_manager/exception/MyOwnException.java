package com.waracle.cake_manager.exception;

public class MyOwnException extends Exception {
    public MyOwnException() {
        super();
    }

    public MyOwnException(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MyOwnException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyOwnException(String message) {
        super(message);
    }

    public MyOwnException(Throwable cause) {
        super(cause);
    }
}
