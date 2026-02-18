package com.marceloneuro.internalfintech.dto;

public record CreateUser(
    String fullname,
    String email,
    String password,
    String cpfCnpj
) {
}
