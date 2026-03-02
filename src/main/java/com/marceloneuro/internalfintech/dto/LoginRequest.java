package com.marceloneuro.internalfintech.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email field cannot be empty.")
        String email,

        @NotBlank(message = "Password field cannot be empty")
        String password
) {
}
