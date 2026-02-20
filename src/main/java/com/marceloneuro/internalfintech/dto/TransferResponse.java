package com.marceloneuro.internalfintech.dto;

import com.marceloneuro.internalfintech.model.Transaction;

import java.math.BigDecimal;

public record TransferResponse(
        String id,
        BigDecimal amount,
        String senderWalletId,
        String receiverWalletId
) {

    public TransferResponse(Transaction entity) {
        this(
                entity.getId().toString(),
                entity.getAmount(),
                entity.getWalletSender().getId().toString(),
                entity.getWalletReceiver().getId().toString()
        );
    }
}
