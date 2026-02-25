package com.marceloneuro.internalfintech.service.strategy.loans;

import com.marceloneuro.internalfintech.model.enums.LoanType;

import java.math.BigDecimal;

public interface InterestCalculationStrategy {
    BigDecimal calculateTotalAmount(BigDecimal principalAmount);
    LoanType getLoanType();
}
