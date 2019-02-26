package com.app.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExceptionInfo {
    private ExceptionCode code;
    private String message;
    private LocalDateTime exceptionDateTime;

    public ExceptionInfo(ExceptionCode code, String message) {
        this.code = code;
        this.message = message;
        this.exceptionDateTime = LocalDateTime.now();
    }
}
