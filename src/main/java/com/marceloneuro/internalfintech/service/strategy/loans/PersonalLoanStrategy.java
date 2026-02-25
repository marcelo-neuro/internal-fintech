package com.marceloneuro.internalfintech.service.strategy.loans;

import com.marceloneuro.internalfintech.model.enums.LoanType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PersonalLoanStrategy implements InterestCalculationStrategy{
    @Override
    public BigDecimal calculateTotalAmount(BigDecimal principalAmount) {
        return principalAmount.multiply(new BigDecimal("1.10"));
    }

    @Override
    public LoanType getLoanType() {
        return LoanType.PERSONAL;
    }
}
