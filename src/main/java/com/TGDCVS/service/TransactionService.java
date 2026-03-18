package com.TGDCVS.service;

import com.TGDCVS.dto.TransactionDTO;
import java.util.List;

public interface TransactionService {
    void processTransactions(List<TransactionDTO> dtos);
}