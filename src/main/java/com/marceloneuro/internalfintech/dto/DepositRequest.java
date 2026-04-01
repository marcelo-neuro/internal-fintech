package com.marceloneuro.internalfintech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DepositRequest(
        @NotNull(message = "Transaction must have an amount.")
        @Positive(message = "Transaction amount must be greater than 0.")
        BigDecimal amount,

        @NotBlank(message = "Deposit must have a receiver.")
        String receiverWalletId
) {
}
