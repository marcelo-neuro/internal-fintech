package com.marceloneuro.internalfintech.dto;

import com.marceloneuro.internalfintech.model.Loan;
import com.marceloneuro.internalfintech.model.enums.LoanType;

import java.math.BigDecimal;

public record LoanResponse(
        String id,
        BigDecimal amount,
        BigDecimal totalAmountDue,
        LoanType loanType
) {
    public LoanResponse(Loan entity) {
        this(entity.getId().toString(), entity.getAmountRequested(),
                entity.getTotalAmountDue(), entity.getLoanType());
    }
}
