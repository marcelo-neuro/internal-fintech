package com.marceloneuro.internalfintech.dto;

import com.marceloneuro.internalfintech.model.Transaction;

import java.math.BigDecimal;

public record DepositResponse(
        String id,
        BigDecimal amount,
        String receiverWalletId
) {

    public DepositResponse(Transaction entity) {
        this(
                entity.getId().toString(),
                entity.getAmount(),
                entity.getWalletReceiver().getId().toString()
        );
    }
}
