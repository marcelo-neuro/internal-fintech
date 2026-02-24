package com.marceloneuro.internalfintech.controller.handler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldMessageResponse {
    private String field;
    private String message;
}
