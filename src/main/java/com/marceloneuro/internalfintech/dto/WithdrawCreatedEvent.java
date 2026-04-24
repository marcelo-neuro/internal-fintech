package com.marceloneuro.internalfintech.dto;

import com.marceloneuro.internalfintech.model.Transaction;

import java.math.BigDecimal;

public record WithdrawCreatedEvent(
        String senderEmail,
        String senderFullName,
        BigDecimal amount
) {

    public WithdrawCreatedEvent(Transaction entity) {
        this(
                entity.getWalletSender().getUser().getEmail(),
                entity.getWalletSender().getUser().getFullName(),
                entity.getAmount()
        );
    }
}
