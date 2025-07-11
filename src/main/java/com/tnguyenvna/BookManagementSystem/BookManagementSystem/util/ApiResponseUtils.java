package com.tnguyenvna.BookManagementSystem.BookManagementSystem.util;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.tnguyenvna.BookManagementSystem.BookManagementSystem.dto.response.ApiResponse;

public class ApiResponseUtils {

    public static ApiResponse buildSuccessResponse(Object d, HttpStatus status) {
        return ApiResponse.builder()
                .message("successful")
                .data(d).status(status)
                .dateTime(LocalDateTime.now())
                .build();
    }

    public static ApiResponse buildErrorResponse(Object d, HttpStatus status) {
        return ApiResponse.builder()
                .message("Error Occurred:")
                .status(status)
                .data(d)
                .dateTime(LocalDateTime.now())
                .build();
    }
}
