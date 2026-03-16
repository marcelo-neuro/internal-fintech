package com.marceloneuro.internalfintech.service;

import com.marceloneuro.internalfintech.dto.LoanRequest;
import com.marceloneuro.internalfintech.dto.LoanResponse;
import com.marceloneuro.internalfintech.model.Loan;
import com.marceloneuro.internalfintech.model.User;
import com.marceloneuro.internalfintech.model.Wallet;
import com.marceloneuro.internalfintech.model.enums.LoanType;
import com.marceloneuro.internalfintech.repository.LoanRepository;
import com.marceloneuro.internalfintech.repository.WalletRepository;
import com.marceloneuro.internalfintech.service.exceptions.ResourceNotFoundException;
import com.marceloneuro.internalfintech.service.strategy.loans.InterestCalculationStrategy;
import com.marceloneuro.internalfintech.service.strategy.loans.LoanStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private LoanStrategyFactory loanStrategyFactory;

    @Mock
    private InterestCalculationStrategy interestCalculationStrategy;

    @InjectMocks
    private LoanService loanService;

    private User loggedUser;
    private Wallet destinationWallet;
    private UUID userId;
    private UUID walletId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        walletId = UUID.randomUUID();

        loggedUser = new User();
        loggedUser.setId(userId);

        destinationWallet = new Wallet();
        destinationWallet.setId(walletId);
        loggedUser.setWallets(List.of(destinationWallet));
    }

    @Test
    @DisplayName("Must approve personal loan, calculate interest and save data successfully.")
    void requestLoan_ShouldSucceed_WhenPersonalLoanRequested() {
        destinationWallet.setBalance(new BigDecimal("100.00"));

        LoanRequest request = new LoanRequest(
                walletId.toString(),
                new BigDecimal("1000.00"),
                LoanType.PERSONAL
        );

        BigDecimal expectedDebt = new BigDecimal("1100.00");

        when(walletRepository.findByIdAndUserId(walletId, userId))
                .thenReturn(Optional.of(destinationWallet));

        when(loanStrategyFactory.getStrategy(LoanType.PERSONAL))
                .thenReturn(interestCalculationStrategy);

        when(interestCalculationStrategy.calculateTotalAmount(request.amount()))
                .thenReturn(expectedDebt);

        Loan mockSavedLoan = new Loan();
        mockSavedLoan.setId(UUID.randomUUID());
        mockSavedLoan.setTotalAmountDue(expectedDebt);

        when(loanRepository.save(any(Loan.class)))
                .thenReturn(mockSavedLoan);

        LoanResponse response = loanService.createLoan(request, loggedUser);

        assertNotNull(response);
        assertEquals(expectedDebt, response.totalAmountDue());
        assertEquals(new BigDecimal("1100.00"), destinationWallet.getBalance());

        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    @DisplayName("Must throw ResourceNotFoundException when wallet don't exists or don't belong to logged user.")
    void requestLoan_ShouldThrowResourceNotFoundException_WhenWalletDoNotExistsOrBelongToLoggedUser() {
        destinationWallet.setBalance(new BigDecimal("50.00"));

        when(walletRepository.findByIdAndUserId(walletId, loggedUser.getId()))
                .thenReturn(Optional.empty());

        LoanRequest request = new LoanRequest(
                walletId.toString(),
                new BigDecimal("1000.00"),
                LoanType.PERSONAL
        );

        ResourceNotFoundException expectedException = assertThrows(
                ResourceNotFoundException.class, () -> loanService.createLoan(request, loggedUser)
        );

        assertEquals("Receiver not found, or access denied.", expectedException.getMessage());

        verify(loanRepository, never()).save(any());
    }
}
