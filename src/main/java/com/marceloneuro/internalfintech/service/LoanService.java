package com.marceloneuro.internalfintech.service;

import com.marceloneuro.internalfintech.dto.LoanRequest;
import com.marceloneuro.internalfintech.dto.LoanResponse;
import com.marceloneuro.internalfintech.model.Loan;
import com.marceloneuro.internalfintech.model.User;
import com.marceloneuro.internalfintech.model.Wallet;
import com.marceloneuro.internalfintech.repository.LoanRepository;
import com.marceloneuro.internalfintech.repository.WalletRepository;
import com.marceloneuro.internalfintech.service.exceptions.ResourceNotFoundException;
import com.marceloneuro.internalfintech.service.strategy.loans.InterestCalculationStrategy;
import com.marceloneuro.internalfintech.service.strategy.loans.LoanStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final WalletRepository walletRepository;
    private final LoanStrategyFactory loanStrategyFactory;

    @Transactional
    public LoanResponse createLoan(LoanRequest loanRequest, User loggedUser) {
        Wallet receiverWallet = walletRepository
                .findByIdAndUserId(UUID.fromString(loanRequest.walletId()), loggedUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found, or access denied."));

        InterestCalculationStrategy interestCalculation = loanStrategyFactory.getStrategy(loanRequest.loanType());

        Loan newLoan = new Loan();
        newLoan.setAmountRequested(loanRequest.amount());
        newLoan.setWallet(receiverWallet);
        newLoan.setLoanType(loanRequest.loanType());
        newLoan.setTotalAmountDue(interestCalculation.calculateTotalAmount(loanRequest.amount()));

        receiverWallet.setBalance(receiverWallet.getBalance().add(loanRequest.amount()));

        Loan savedLoan = loanRepository.save(newLoan);

        return new LoanResponse(savedLoan);
    }
}
