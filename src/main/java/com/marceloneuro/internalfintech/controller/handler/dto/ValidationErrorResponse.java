package com.marceloneuro.internalfintech.controller.handler.dto;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationErrorResponse extends StandardErrorResponse {

    private List<FieldMessageResponse> errors = new ArrayList<>();

    public ValidationErrorResponse(Instant timestamp, Integer status, String message, String path) {
        super(timestamp, status, message, path);
    }

    public void addError(String field, String message) {
        errors.add(new FieldMessageResponse(field, message));
    }
}
