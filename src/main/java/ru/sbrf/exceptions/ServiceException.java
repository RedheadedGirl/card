package ru.sbrf.exceptions;

import org.springframework.http.HttpStatus;

public abstract class ServiceException extends RuntimeException {

    private final ErrorInfo errorInfo;

    public ServiceException(ErrorInfo errorInfo) {
        this(errorInfo, null);
    }

    public ServiceException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo.getMessage(), cause);
        this.errorInfo = errorInfo;
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    public abstract HttpStatus getHttpStatus();
}