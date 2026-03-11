package com.valhala.optimacvspring.common.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> validationErrors
) {
    public ApiErrorResponse(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, null);
    }
}
