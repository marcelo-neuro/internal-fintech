package com.marceloneuro.internalfintech.service;

import com.marceloneuro.internalfintech.dto.CreateUserRequest;
import com.marceloneuro.internalfintech.dto.UserResponse;
import com.marceloneuro.internalfintech.model.User;
import com.marceloneuro.internalfintech.model.Wallet;
import com.marceloneuro.internalfintech.model.WalletType;
import com.marceloneuro.internalfintech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(CreateUserRequest createUserRequest) {

        if(userRepository.existsByEmail()) {
            throw new IllegalArgumentException("Email already exists.");
        }

        User newUser = new User();
        copyCreateUserDtoToEntity(newUser, createUserRequest);
        newUser.setPassword(passwordEncoder.encode(createUserRequest.password()));


        Wallet newUserWallet = new Wallet();
        newUserWallet.setBalance(BigDecimal.ZERO);
        newUserWallet.setType(WalletType.SAVING);

        newUserWallet.setUser(newUser);
        newUser.setWallets(List.of(newUserWallet));

        User savedUser = userRepository.save(newUser);

        return new UserResponse(savedUser);
    }

    public void copyCreateUserDtoToEntity(User entity, CreateUserRequest dto) {
        entity.setEmail(dto.email());
        entity.setCpfCnpj(dto.cpfCnpj());
        entity.setFullName(dto.fullname());
    }
}
