package ru.sbrf.exceptions;

import org.springframework.http.HttpStatus;

public class AlreadyInUseException extends ServiceException {

    public AlreadyInUseException(String message) {
        super(new ErrorInfo("BAD_REQUEST", message));
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
