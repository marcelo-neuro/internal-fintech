package com.marceloneuro.internalfintech.service;

import com.marceloneuro.internalfintech.dto.TransferRequest;
import com.marceloneuro.internalfintech.model.User;
import com.marceloneuro.internalfintech.model.Wallet;
import com.marceloneuro.internalfintech.repository.WalletRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private TransactionService transactionService;

    private User loggedUser;
    private Wallet senderWallet;
    private UUID senderId;
    private UUID senderWalletId;

    @BeforeEach
    void setUp() {
        senderId = UUID.randomUUID();
        senderWalletId = UUID.randomUUID();

        loggedUser = new User();
        loggedUser.setId(senderId);

        senderWallet = new Wallet();
        senderWallet.setId(senderWalletId);
        senderWallet.setUser(loggedUser);
    }

    @Test
    @DisplayName("Must throw exception when balance be insufficient for transaction.")
    void transfer_ShouldThrowException_WhenBalanceIsInsufficient() {
        senderWallet.setBalance(new BigDecimal("50.00"));

        TransferRequest request = new TransferRequest(
                new BigDecimal("100.00"),
                senderWalletId.toString(),
                UUID.randomUUID().toString()
        );

        when(walletRepository.findByIdAndUserId(senderWalletId, senderId))
                .thenReturn(Optional.of(senderWallet));

        IllegalArgumentException expectedException = assertThrows(
                IllegalArgumentException.class, () -> transactionService.transfer(request, loggedUser)
        );

        assertEquals("The sender balance is insufficient for this transaction.", expectedException.getMessage());

    }
}
