package com.waracle.cake_manager.exception;

/**
 * This is an unchecked exception, so no need to handle or declare it
 */
public class DeliberateException extends RuntimeException {
    public DeliberateException() {
        super();
    }

    public DeliberateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DeliberateException(final String message) {
        super(message);
    }

    public DeliberateException(final Throwable cause) {
        super(cause);
    }
}
