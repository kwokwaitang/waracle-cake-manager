package com.waracle.cake_manager.exception;

public class MyOwnRuntimeException extends RuntimeException /* Unchecked - so don't have to be handled or declared */ {

    public MyOwnRuntimeException() {
        super();
    }

    public MyOwnRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MyOwnRuntimeException(final String message) {
        super(message);
    }

    public MyOwnRuntimeException(final Throwable cause) {
        super(cause);
    }
}
