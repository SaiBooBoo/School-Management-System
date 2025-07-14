package org.example.athenabackend.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ApiError {
    private String message;
    private int code;
    private LocalDateTime timestamp;

    public ApiError(String message, int code, LocalDateTime timestamp) {
        this.message = message;
        this.code = code;
        this.timestamp = timestamp;
    }
}
