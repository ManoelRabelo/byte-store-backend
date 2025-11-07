package com.bytestore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        String errorCode,
        List<FieldError> fieldErrors
) {
    public ErrorResponseDTO {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    public ErrorResponseDTO(int status, String error, String message, String path, String errorCode) {
        this(LocalDateTime.now(), status, error, message, path, errorCode, null);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record FieldError(
            String field,
            String message,
            Object rejectedValue
    ) {
    }
}

