package com.lsyf.pay.logger.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = -1L;
    private boolean success;
    private T result;
    private String error;


    public static <T> ApiResponse success(T t) {
        return ApiResponse.builder()
                .success(true)
                .result(t)
                .build();
    }

    public static ApiResponse fail(String error) {
        return fail(error, null);
    }

    public static <T> ApiResponse fail(String error, T t) {
        return ApiResponse.builder()
                .success(false)
                .result(t)
                .error(error)
                .build();
    }
}