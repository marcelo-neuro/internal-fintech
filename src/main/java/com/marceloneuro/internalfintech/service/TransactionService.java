package com.marceloneuro.internalfintech.service;

import com.marceloneuro.internalfintech.dto.*;
import com.marceloneuro.internalfintech.model.User;
import com.marceloneuro.internalfintech.model.enums.OperationType;
import com.marceloneuro.internalfintech.model.Transaction;
import com.marceloneuro.internalfintech.model.Wallet;
import com.marceloneuro.internalfintech.repository.TransactionRepository;
import com.marceloneuro.internalfintech.repository.WalletRepository;
import com.marceloneuro.internalfintech.security.CustomUserDetails;
import com.marceloneuro.internalfintech.service.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public TransferResponse transfer(TransferRequest transferRequest, User loggedUser) {
        // Validate if the receiver and the sender are the same wallet.
        if(transferRequest.receiverWalletId().equals(transferRequest.senderWalletId())) {
            throw new IllegalArgumentException("The receiver and the sender cannot be the same wallet.");
        }

        Wallet senderWallet = walletRepository
                .findByIdAndUserId(UUID.fromString(transferRequest.senderWalletId()), loggedUser.getId())
                .orElseThrow(() -> {
                   return new ResourceNotFoundException("Wallet not found, or access denied.");
                });

        // Validate if the sender have sufficient balance for transaction.
        if (senderWallet.getBalance().compareTo(transferRequest.amount()) < 0) {
            throw new IllegalArgumentException("The sender balance is insufficient for this transaction.");
        }

        Wallet receiverWallet = walletRepository.findById(UUID.fromString(transferRequest.receiverWalletId()))
                .orElseThrow(() -> new ResourceNotFoundException("Receiver wallet not found."));

        // Update balance values.
        senderWallet.setBalance(senderWallet.getBalance().subtract(transferRequest.amount()));
        receiverWallet.setBalance(receiverWallet.getBalance().add(transferRequest.amount()));

        //Create Transaction
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transferRequest.amount());
        newTransaction.setWalletSender(senderWallet);
        newTransaction.setWalletReceiver(receiverWallet);
        newTransaction.setOperationType(OperationType.TRANSFERENCE);

        // Save entities.
        Transaction savedTransaction = transactionRepository.save(newTransaction);

        return new TransferResponse(savedTransaction);
    }

    @Transactional
    public DepositResponse deposit(DepositRequest depositRequest) {
        Wallet receiverWallet = walletRepository.findById(UUID.fromString(depositRequest.receiverWalletId()))
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found."));

        receiverWallet.setBalance(receiverWallet.getBalance().add(depositRequest.amount()));

        Transaction newTransaction = new Transaction();
        newTransaction.setOperationType(OperationType.CASH_IN);
        newTransaction.setAmount(depositRequest.amount());
        newTransaction.setWalletReceiver(receiverWallet);

        Transaction savedTransaction = transactionRepository.save(newTransaction);

        return new DepositResponse(savedTransaction);
    }

    @Transactional
    public WithdrawResponse withdraw(WithdrawRequest withdrawRequest, User loggedUser) {
        Wallet senderWallet = walletRepository
                .findByIdAndUserId(UUID.fromString(withdrawRequest.senderWalletId()), loggedUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found, or access denied."));

        if (senderWallet.getBalance().compareTo(withdrawRequest.amount()) < 0) {
            throw new IllegalArgumentException("The sender balance is insufficient for this transaction.");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(withdrawRequest.amount()));

        Transaction newTransaction = new Transaction();
        newTransaction.setWalletSender(senderWallet);
        newTransaction.setAmount(withdrawRequest.amount());
        newTransaction.setOperationType(OperationType.CASH_OUT);

        Transaction savedTransaction = transactionRepository.save(newTransaction);

        return new WithdrawResponse(savedTransaction);
    }
}
