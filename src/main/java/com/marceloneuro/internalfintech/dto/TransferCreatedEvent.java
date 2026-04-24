package com.marceloneuro.internalfintech.dto;

import com.marceloneuro.internalfintech.model.Transaction;

import java.math.BigDecimal;

public record TransferCreatedEvent(
        String senderEmail,
        String senderFullName,
        String receiverEmail,
        String receiverFullName,
        BigDecimal amount
) {

    public TransferCreatedEvent(Transaction entity) {
        this(
                entity.getWalletSender().getUser().getEmail(),
                entity.getWalletSender().getUser().getFullName(),
                entity.getWalletReceiver().getUser().getEmail(),
                entity.getWalletReceiver().getUser().getFullName(),
                entity.getAmount()
        );
    }
}
