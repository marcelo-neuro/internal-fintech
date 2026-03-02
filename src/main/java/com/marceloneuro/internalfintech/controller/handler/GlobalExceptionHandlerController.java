package com.marceloneuro.internalfintech.controller.handler;

import com.marceloneuro.internalfintech.controller.handler.dto.StandardErrorResponse;
import com.marceloneuro.internalfintech.controller.handler.dto.ValidationErrorResponse;
import com.marceloneuro.internalfintech.service.exceptions.MissingTokenException;
import com.marceloneuro.internalfintech.service.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardErrorResponse> handleResourceNotFound(ResourceNotFoundException e,
                                                                        HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardErrorResponse error = new StandardErrorResponse(Instant.now(), status.value(),
                e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardErrorResponse> handleIIllegalArgument(IllegalArgumentException e,
                                                                        HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardErrorResponse error = new StandardErrorResponse(Instant.now(), status.value(),
                e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                                HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;

        ValidationErrorResponse errors = new ValidationErrorResponse(Instant.now(), status.value(),
                "Validation error in the submitted data", request.getRequestURI());

        e.getBindingResult()
                .getFieldErrors()
                .forEach(fe -> errors.addError(fe.getField(), fe.getDefaultMessage()));

        return ResponseEntity.status(status).body(errors);
    }

    @ExceptionHandler(MissingTokenException.class)
    public ResponseEntity<StandardErrorResponse> handleMissingToken(MissingTokenException e,
                                                                    HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardErrorResponse error = new StandardErrorResponse(Instant.now(), status.value(),
                e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status.value()).body(error);
    }
}
