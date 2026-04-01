package com.marceloneuro.internalfintech.dto;

import com.marceloneuro.internalfintech.model.Transaction;

import java.math.BigDecimal;

public record WithdrawResponse(
        String id,
        BigDecimal amount,
        String senderWalletId
) {

    public WithdrawResponse(Transaction entity) {
        this(
                entity.getId().toString(),
                entity.getAmount(),
                entity.getWalletSender().getId().toString()
        );
    }
}
