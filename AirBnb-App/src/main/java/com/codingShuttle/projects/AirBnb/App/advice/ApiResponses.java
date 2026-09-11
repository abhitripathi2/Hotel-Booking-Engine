package com.codingShuttle.projects.AirBnb.App.advice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponses<T> {

    private LocalDateTime timestamp;
    private T data;
    private ApiError error;

    private ApiResponses() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponses(T data) {
        this();
        this.data = data;
    }

    public ApiResponses(ApiError error) {
        this();
        this.error = error;
    }
}
