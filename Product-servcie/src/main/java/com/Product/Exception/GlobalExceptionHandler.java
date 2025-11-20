package com.Product.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,Object>> handleRuntimeException (RuntimeException exception){
        Map <String , Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Bad Request");
        error.put("message", exception.getMessage());
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String , Object>> handleValidationException (
    MethodArgumentNotValidException exception)
    {
        Map<String , Object> error = new HashMap<>();
        error.put("timestamp",LocalDateTime.now().toString());
        error.put("error", "Validation failed");
        error.put("status" , HttpStatus.BAD_REQUEST.value());

        Map<String, String> fieldErrors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(e->fieldErrors.put(e.getField(),e.getDefaultMessage()));

        error.put("message" , fieldErrors);

         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
