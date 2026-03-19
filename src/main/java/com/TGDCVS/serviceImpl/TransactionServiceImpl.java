package com.TGDCVS.serviceImpl;


import com.TGDCVS.dto.TransactionDTO;
import com.TGDCVS.entity.TransactionEntity;
import com.TGDCVS.enums.ComplianceStatus;
import com.TGDCVS.enums.ValidationStatus;
import com.TGDCVS.repositories.TransactionRepository;
import com.TGDCVS.ruleengine.RuleEngineService;
import com.TGDCVS.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repo;
    private final RuleEngineService ruleEngine;
    private final AuditService auditService;

    @Override
    public void processTransactions(List<TransactionDTO> dtos) {

        for (TransactionDTO dto : dtos) {

            TransactionEntity entity = map(dto);

            List<String> errors = validate(dto);

            if (!errors.isEmpty()) {
                entity.setValidationStatus(ValidationStatus.FAILURE);
                entity.setFailureReason(String.join(",", errors));
                repo.save(entity);
                continue;
            }

            entity.setValidationStatus(ValidationStatus.SUCCESS);

            calculateTax(entity);

            ruleEngine.applyRules(entity);

            repo.save(entity);

            auditService.log("INGESTION", entity.getTransactionId(), dto);
        }
    }

    private TransactionEntity map(TransactionDTO dto) {
        TransactionEntity e = new TransactionEntity();
        e.setTransactionId(dto.getTransactionId());
        e.setDate(dto.getDate());
        e.setCustomerId(dto.getCustomerId());
        e.setAmount(dto.getAmount());
        e.setTaxRate(dto.getTaxRate());
        e.setReportedTax(dto.getReportedTax());
        e.setTransactionType(dto.getTransactionType());
        return e;
    }

    private List<String> validate(TransactionDTO dto) {
        List<String> errors = new ArrayList<>();
        if (dto.getCustomerId() == null)
            errors.add("Missing customerId");

        if (dto.getTaxRate() == null)
            errors.add("Missing taxRate");

        if (dto.getTransactionType() == null)
            errors.add("Missing transactionType");

        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            errors.add("Invalid amount");

        if (dto.getDate() == null)
            errors.add("Invalid date");

        if (dto.getTransactionId() == null)
            errors.add("Missing transactionId");

        return errors;
    }

    public void calculateTax(TransactionEntity e) {

        if (e.getReportedTax() == null) {
            e.setComplianceStatus(ComplianceStatus.NON_COMPLIANT);
            return;
        }

        BigDecimal expected = e.getAmount().multiply(e.getTaxRate());
        BigDecimal gap = expected.subtract(e.getReportedTax());

        e.setExpectedTax(expected);
        e.setTaxGap(gap);

        if (gap.abs().compareTo(BigDecimal.ONE) <= 0)
            e.setComplianceStatus(ComplianceStatus.COMPLIANT);
        else if (gap.compareTo(BigDecimal.ONE) > 0)
            e.setComplianceStatus(ComplianceStatus.UNDERPAID);
        else
            e.setComplianceStatus(ComplianceStatus.OVERPAID);

        auditService.log("TAX_COMPUTATION", e.getTransactionId(), e);
    }
}