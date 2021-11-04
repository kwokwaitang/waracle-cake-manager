package com.waracle.cake_manager.exception;

public class AnotherMyOwnRuntimeException extends RuntimeException {
    public AnotherMyOwnRuntimeException() {
        super();
    }

    public AnotherMyOwnRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AnotherMyOwnRuntimeException(final String message) {
        super(message);
    }

    public AnotherMyOwnRuntimeException(final Throwable cause) {
        super(cause);
    }
}
