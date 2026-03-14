package com.marceloneuro.internalfintech.service;

import com.marceloneuro.internalfintech.dto.TransferRequest;
import com.marceloneuro.internalfintech.dto.TransferResponse;
import com.marceloneuro.internalfintech.model.Transaction;
import com.marceloneuro.internalfintech.model.User;
import com.marceloneuro.internalfintech.model.Wallet;
import com.marceloneuro.internalfintech.repository.TransactionRepository;
import com.marceloneuro.internalfintech.repository.WalletRepository;
import com.marceloneuro.internalfintech.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
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
    @DisplayName("Must transfer successfully, update data and save transaction.")
    void transfer_ShouldSucceed_WhenBalancesAreCorrect() {
        senderWallet.setBalance(new BigDecimal("100.00"));

        Wallet receiverWallet = new Wallet();
        receiverWallet.setId(UUID.randomUUID());
        receiverWallet.setBalance(new BigDecimal("0.00"));

        TransferRequest request = new TransferRequest(
                new BigDecimal("50.00"),
                senderWalletId.toString(),
                receiverWallet.getId().toString()
        );

        Transaction mockedSavedTransaction = new Transaction();
        mockedSavedTransaction.setAmount(new BigDecimal("50.00"));
        mockedSavedTransaction.setId(UUID.randomUUID());
        mockedSavedTransaction.setWalletReceiver(receiverWallet);
        mockedSavedTransaction.setWalletSender(senderWallet);

        when(walletRepository.findByIdAndUserId(senderWalletId, senderId))
                .thenReturn(Optional.of(senderWallet));
        when(walletRepository.findById(receiverWallet.getId()))
                .thenReturn(Optional.of(receiverWallet));

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(mockedSavedTransaction);

        TransferResponse response = transactionService.transfer(request, loggedUser);

        assertNotNull(response);
        assertEquals(new BigDecimal("50.00"), senderWallet.getBalance(), "Sender's balance must be reduced to 50");
        assertEquals(new BigDecimal("50.00"), receiverWallet.getBalance(), "Receiver's balance must be increased to 50");

        verify(transactionRepository, times(1)).save(any());
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
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Must throw exception when the receiver and the sender are the same.")
    void transfer_ShouldThrowException_WhenReceiverAndSenderAreEqual() {
        senderWallet.setBalance(new BigDecimal("50.00"));

        TransferRequest request = new TransferRequest(
                new BigDecimal("50.00"),
                senderWalletId.toString(),
                senderWalletId.toString()
        );

        IllegalArgumentException expectedException = assertThrows(
                IllegalArgumentException.class, () -> transactionService.transfer(request, loggedUser)
        );

        assertEquals("The receiver and the sender cannot be the same wallet.", expectedException.getMessage());

        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Must throw exception when the sender isn't the wallet's owner, or the wallet don't exists.")
    void transfer_ShouldThrowException_WhenSenderDoNotOwnTheWalletOrWalletDoNotExists() {
        senderWallet.setBalance(new BigDecimal("50.00"));

        when(walletRepository.findByIdAndUserId(senderWalletId, loggedUser.getId()))
                .thenReturn(Optional.empty());

        TransferRequest request = new TransferRequest(
                new BigDecimal("50.00"),
                senderWalletId.toString(),
                UUID.randomUUID().toString()
        );

        ResourceNotFoundException expectedException = assertThrows(
                ResourceNotFoundException.class, () -> transactionService.transfer(request, loggedUser)
        );

        assertEquals("Wallet not found, or access denied.", expectedException.getMessage());

        verify(transactionRepository, never()).save(any());
    }
}
