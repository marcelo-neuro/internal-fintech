package com.marceloneuro.internalfintech.dto;

import com.marceloneuro.internalfintech.model.User;

public record UserResponse(
        String id,
        String fullName,
        String email
) {

    public UserResponse(User entity) {
        this(
                entity.getId().toString(),
                entity.getFullName(),
                entity.getEmail()
        );
    }
}
