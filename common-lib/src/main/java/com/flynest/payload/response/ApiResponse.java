package com.flynest.payload.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Data

public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder().success(true).data(data).build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder().success(true).message(message).data(data).build();
    }
}
