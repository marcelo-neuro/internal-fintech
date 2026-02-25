package com.marceloneuro.internalfintech.service.strategy.loans;

import com.marceloneuro.internalfintech.model.enums.LoanType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LoanStrategyFactory {

    private final Map<LoanType, InterestCalculationStrategy> strategies;

    public LoanStrategyFactory(List<InterestCalculationStrategy> strategyList) {
        strategies = strategyList.stream()
                .collect(Collectors.toMap(InterestCalculationStrategy::getLoanType, s -> s));
    }

    public InterestCalculationStrategy getStrategy(LoanType loanType) {
        InterestCalculationStrategy strategy = strategies.get(loanType);
        if (strategy == null) {
            throw new IllegalArgumentException("This loan type isn't supported.");
        }
        return strategy;
    }
}
