package com.marceloneuro.internalfintech.dto;

import com.marceloneuro.internalfintech.model.enums.LoanType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record LoanRequest(
        @NotBlank(message = "Loan must have a wallet.")
        String walletId,

        @NotNull(message = "Loan must have an amount requested.")
        @Positive(message = "Loan amount must be greater than 0.")
        BigDecimal amount,

        @NotNull(message = "Loan must have a type.")
        LoanType loanType
) {
}
