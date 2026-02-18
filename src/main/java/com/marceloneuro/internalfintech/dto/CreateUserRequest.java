package com.marceloneuro.internalfintech.dto;

public record CreateUserRequest(
    String fullname,
    String email,
    String password,
    String cpfCnpj
) {
}
