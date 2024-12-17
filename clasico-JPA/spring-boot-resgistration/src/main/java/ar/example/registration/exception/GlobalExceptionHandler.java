package ar.example.registration.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ar.example.registration.dto.ApiResponse;
import ar.example.registration.util.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> handleGeneralException(Exception ex, HttpServletRequest request) {
        return ResponseUtil.error(Arrays.asList(ex.getMessage()), "An unexpected error occurred", 1001, request.getRequestURI());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<Object> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return ResponseUtil.error(Arrays.asList(ex.getMessage()), "Resource not found", 404, request.getRequestURI());
    }

    @ExceptionHandler(ResponseNotFoundException.class)
    public ApiResponse<Object> handleResponseNotFoundException(ResponseNotFoundException ex, HttpServletRequest request) {
        return ResponseUtil.error(Arrays.asList(ex.getMessage()), "Response data not found", 204, request.getRequestURI());
    }
}
