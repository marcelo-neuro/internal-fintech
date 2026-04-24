package com.marceloneuro.internalfintech.dto;

import com.marceloneuro.internalfintech.model.Transaction;

import java.math.BigDecimal;

public record DepositCreatedEvent(
        String receiverEmail,
        String receiverFullName,
        BigDecimal amount
) {

    public DepositCreatedEvent(Transaction entity) {
        this(
                entity.getWalletReceiver().getUser().getEmail(),
                entity.getWalletReceiver().getUser().getFullName(),
                entity.getAmount()
        );
    }
}
