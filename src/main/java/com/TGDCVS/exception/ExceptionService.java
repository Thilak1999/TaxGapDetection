package com.TGDCVS.exception;

import com.TGDCVS.entity.ExceptionEntity;
import com.TGDCVS.entity.TransactionEntity;
import com.TGDCVS.repositories.ExceptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExceptionService {

    private final ExceptionRepository repo;

    public void create(TransactionEntity txn, String rule, String severity, String msg) {

        ExceptionEntity ex = new ExceptionEntity();
        ex.setTransactionId(txn.getTransactionId());
        ex.setCustomerId(txn.getCustomerId());
        ex.setRuleName(rule);
        ex.setSeverity(severity);
        ex.setMessage(msg);
        ex.setTimestamp(LocalDateTime.now());

        repo.save(ex);
    }
}