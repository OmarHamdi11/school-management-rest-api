package com.springboot.school_management.exception;

import com.springboot.school_management.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request
    ){
        ApiResponse<Object> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setPath(request.getDescription(false).replace("uri:",""));

        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request
    ){
        ApiResponse<Object> response = ApiResponse.error(
                HttpStatus.FORBIDDEN.value(),
                "You don't have permission to access this resource"
        );
        response.setPath(request.getDescription(false).replace("uri:",""));

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN.value())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(
            Exception ex, WebRequest request
    ){
        ApiResponse<Object> response = ApiResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                "An error occurred: " + ex.getMessage()
        );
        response.setPath(request.getDescription(false).replace("uri:",""));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(response);

    }

}
