package com.marceloneuro.internalfintech.repository;

import com.marceloneuro.internalfintech.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    Optional<Wallet> findByIdAndUserId(UUID walletId, UUID userId);
}
