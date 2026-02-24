package com.marceloneuro.internalfintech.dto;

public record CreateUserRequest(
    String fullName,
    String email,
    String password,
    String cpfCnpj
) {
}
