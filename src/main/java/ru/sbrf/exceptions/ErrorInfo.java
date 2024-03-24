package ru.sbrf.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ErrorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String code;
    protected String message;

    public ErrorInfo(String code) {
        this.code = code;
    }

    public ErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }
}