package com.marceloneuro.internalfintech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull(message = "Transaction must have an amount.")
        @Positive(message = "Transaction amount must be greater than 0.")
        BigDecimal amount,

        @NotBlank(message = "Transaction must have a sender.")
        String senderWalletId,

        @NotBlank(message = "Transaction must have a receiver.")
        String receiverWalletId
) {
}
