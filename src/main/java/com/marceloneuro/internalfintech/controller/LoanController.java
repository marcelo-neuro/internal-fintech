package com.marceloneuro.internalfintech.controller;

import com.marceloneuro.internalfintech.dto.LoanRequest;
import com.marceloneuro.internalfintech.dto.LoanResponse;
import com.marceloneuro.internalfintech.security.CustomUserDetails;
import com.marceloneuro.internalfintech.service.LoanService;
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
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody LoanRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        LoanResponse response = loanService.createLoan(request, userDetails.getUser());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }
}
