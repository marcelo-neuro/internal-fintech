package com.marceloneuro.internalfintech.service.strategy.loans;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PayrollLoanStrategyTest {

    @Test
    @DisplayName("Must calculate the interest accurately with 5% rate for Payroll Loan.")
    void calculateTotalAmount_ShouldCalculateInterestAccurately_WhenAmountIsProvided() {
        PayrollLoanStrategy payrollLoanStrategy = new PayrollLoanStrategy();
        BigDecimal expectedInterestCalculated = new BigDecimal("1050.00");
        BigDecimal requestedAmount = new BigDecimal("1000.00");

        BigDecimal result = payrollLoanStrategy.calculateTotalAmount(requestedAmount);
        assertEquals(0, expectedInterestCalculated.compareTo(result));
    }
}
