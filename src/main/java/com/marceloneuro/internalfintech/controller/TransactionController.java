package com.marceloneuro.internalfintech.controller;

import com.marceloneuro.internalfintech.dto.TransferRequest;
import com.marceloneuro.internalfintech.dto.TransferResponse;
import com.marceloneuro.internalfintech.security.CustomUserDetails;
import com.marceloneuro.internalfintech.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        TransferResponse response = transactionService.transfer(request, userDetails.getUser());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }
}
