package com.TGDCVS.serviceImpl;

import com.TGDCVS.entity.ExceptionEntity;
import com.TGDCVS.entity.TransactionEntity;
import com.TGDCVS.repositories.ExceptionRepository;
import com.TGDCVS.service.ExceptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExceptionServiceImpl implements ExceptionService {

    private final ExceptionRepository repo;

    @Override
    public void create(TransactionEntity txn, String ruleName, String severity, String message) {

        ExceptionEntity ex = new ExceptionEntity();

        ex.setTransactionId(txn.getTransactionId());
        ex.setCustomerId(txn.getCustomerId());
        ex.setRuleName(ruleName);
        ex.setSeverity(severity);
        ex.setMessage(message);
        ex.setTimestamp(LocalDateTime.now());

        repo.save(ex);
    }

    @Override
    public List<ExceptionEntity> getAll() {
        return repo.findAll();
    }

    @Override
    public List<ExceptionEntity> search(String customerId, String severity, String ruleName) {

        if (customerId != null)
            return repo.findByCustomerId(customerId);

        if (severity != null)
            return repo.findBySeverity(severity);

        if (ruleName != null)
            return repo.findByRuleName(ruleName);

        return repo.findAll();
    }
}