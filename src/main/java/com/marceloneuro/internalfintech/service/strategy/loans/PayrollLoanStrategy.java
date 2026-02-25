package com.marceloneuro.internalfintech.service.strategy.loans;

import com.marceloneuro.internalfintech.model.enums.LoanType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PayrollLoanStrategy implements InterestCalculationStrategy{
    @Override
    public BigDecimal calculateTotalAmount(BigDecimal principalAmount) {
        return principalAmount.multiply(new BigDecimal("1.05"));
    }

    @Override
    public LoanType getLoanType() {
        return LoanType.PAYROLL;
    }
}
