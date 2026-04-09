package com.example.bloodbank.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Response> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request){
        Response response = new Response();
        response.setErrMsg(ex.getErrMsg());
        response.setErrCode(ex.getErrCode());
        response.setPath(request.getRequestURI());
        response.setDateTime(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(ex.getErrCode()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> handleRunTimeException(RuntimeException ex, HttpServletRequest request){
        // Let Spring Security exceptions propagate normally
        if (ex instanceof AuthenticationException) {
            throw ex;
        }
        Response response = new Response();
        response.setErrMsg(ex.getMessage());
        response.setErrCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setPath(request.getRequestURI());
        response.setDateTime(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNBotValidException(MethodArgumentNotValidException ex){
        Map<String, String> map = new HashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> map.put(e.getField(), e.getDefaultMessage()));
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
