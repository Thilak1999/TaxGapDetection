package com.TGDCVS.controller;

import com.TGDCVS.dto.TransactionDTO;
import com.TGDCVS.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestBody List<TransactionDTO> dtos) {
        service.processTransactions(dtos);
        return ResponseEntity.ok("Processed Successfully");
    }
}