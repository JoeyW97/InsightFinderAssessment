package com.insightfinder.exceptions;

public class ErrorMessageLoaderException extends RuntimeException {
    public ErrorMessageLoaderException(String fileName, Throwable cause) {
        super("Error when loading error message file " + fileName, cause);
    }
}
