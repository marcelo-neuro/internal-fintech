package com.marceloneuro.internalfintech.service.strategy.loans;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PersonalLoanStrategyTest {

    @Test
    @DisplayName("Must calculate the interest accurately with 10% rate for Personal Loan.")
    void calculateTotalAmount_ShouldCalculateInterestAccurately_WhenAmountIsProvided() {
        PersonalLoanStrategy personalLoanStrategy = new PersonalLoanStrategy();

        BigDecimal expectedInterestCalculated = new BigDecimal("1100.00");
        BigDecimal requestedAmount = new BigDecimal("1000.00");

        BigDecimal result = personalLoanStrategy.calculateTotalAmount(requestedAmount);
        assertEquals(0, expectedInterestCalculated.compareTo(result));
    }
}
