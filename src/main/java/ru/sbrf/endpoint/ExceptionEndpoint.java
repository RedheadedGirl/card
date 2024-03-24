package ru.sbrf.endpoint;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.sbrf.exceptions.ErrorInfo;
import ru.sbrf.exceptions.ServiceException;

@RestControllerAdvice
public class ExceptionEndpoint extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(status)
                .body(new ErrorInfo(HttpStatus.resolve(status.value()).name(), ex.getMessage()));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorInfo> onServiceException(ServiceException ex) {
//        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getErrorInfo());
    }
}
