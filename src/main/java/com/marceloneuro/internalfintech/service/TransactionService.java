package com.marceloneuro.internalfintech.service;

import com.marceloneuro.internalfintech.dto.TransferRequest;
import com.marceloneuro.internalfintech.dto.TransferResponse;
import com.marceloneuro.internalfintech.model.OperationType;
import com.marceloneuro.internalfintech.model.Transaction;
import com.marceloneuro.internalfintech.model.Wallet;
import com.marceloneuro.internalfintech.repository.TransactionRepository;
import com.marceloneuro.internalfintech.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public TransferResponse transfer(TransferRequest transferRequest) {
        // Validate if the receiver and the sender are the same wallet.
        if(transferRequest.receiverWalletId().equals(transferRequest.senderWalletId())) {
            throw new IllegalArgumentException("The receiver and the sender cannot be the same wallet.");
        }

        Wallet senderWallet = walletRepository.findById(UUID.fromString(transferRequest.senderWalletId()))
                .orElseThrow(() -> new IllegalArgumentException("Sender wallet not found."));

        // Validate if the sender have sufficient balance for transaction.
        if (senderWallet.getBalance().compareTo(transferRequest.amount()) < 0) {
            throw new IllegalArgumentException("The sender balance is insufficient for this transaction.");
        }

        Wallet receiverWallet = walletRepository.findById(UUID.fromString(transferRequest.receiverWalletId()))
                .orElseThrow(() -> new IllegalArgumentException("Receiver wallet not found."));

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


}
