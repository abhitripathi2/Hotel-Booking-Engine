package com.codingShuttle.projects.AirBnb.App.advice;


import com.codingShuttle.projects.AirBnb.App.exception.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponses<?>> handleResourceNotFound(ResourceNotFoundException exception) {
        ApiError apiError = ApiError.builder().status(HttpStatus.NOT_FOUND).message(exception.getMessage()).build();

        return buildErrorResponseEntity(apiError);
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponses<?>> handleAuthenticationException(AuthenticationException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .build();

        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponses<?>> handleJwtException(JwtException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .build();

        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponses<?>> handleAccessDeniedException(AccessDeniedException exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .message(exception.getMessage())
                .build();

        return buildErrorResponseEntity(apiError);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponses<?>> handleInternalServerError(Exception exception) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .build();

        return buildErrorResponseEntity(apiError);
    }

    private ResponseEntity<ApiResponses<?>> buildErrorResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(new ApiResponses<>(apiError), apiError.getStatus());
    }
}
