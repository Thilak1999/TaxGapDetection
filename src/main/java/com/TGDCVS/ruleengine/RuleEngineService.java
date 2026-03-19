package com.TGDCVS.ruleengine;

import com.TGDCVS.entity.RuleEntity;
import com.TGDCVS.entity.TransactionEntity;
import com.TGDCVS.enums.TransactionType;
import com.TGDCVS.exception.ExceptionService;
import com.TGDCVS.repositories.RuleRepository;
import com.TGDCVS.repositories.TransactionRepository;
import com.TGDCVS.serviceImpl.AuditService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RuleEngineService {

    private final RuleRepository ruleRepo;
    private final ExceptionService exceptionService;
    private final TransactionRepository transactionRepository;
    private final ObjectMapper mapper;
    private final AuditService auditService;

    public void applyRules(TransactionEntity txn) {

        List<RuleEntity> rules = ruleRepo.findByEnabledTrue();

        for (RuleEntity rule : rules) {

            auditService.log(
                    "RULE_EXECUTION",
                    txn.getTransactionId(),
                    rule.getRuleName()
            );

            switch (rule.getRuleName()) {

                case "HIGH_VALUE":
                    applyHighValue(txn, rule);
                    break;

                case "REFUND_VALIDATION":
                    applyRefundRule(txn, rule);
                    break;

                case "GST_SLAB":
                    applyGstRule(txn, rule);
                    break;
            }
        }
    }

    private void applyHighValue(TransactionEntity txn, RuleEntity rule) {
        try {
            JsonNode node = mapper.readTree(rule.getConfigJson());
            BigDecimal threshold = new BigDecimal(node.get("threshold").asText());

            if (txn.getAmount().compareTo(threshold) > 0) {
                exceptionService.create(txn, "HIGH_VALUE", "HIGH", "High value transaction");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void applyRefundRule(TransactionEntity txn, RuleEntity rule) {

        if (txn.getTransactionType() != TransactionType.REFUND)
            return;

        TransactionEntity sale = transactionRepository
                .findTopByCustomerIdAndTransactionTypeOrderByDateDesc(
                        txn.getCustomerId(),
                        TransactionType.SALE
                )
                .orElse(null);

        if (sale != null && txn.getAmount().compareTo(sale.getAmount()) > 0) {

            exceptionService.create(
                    txn,
                    "REFUND_VALIDATION",
                    "HIGH",
                    "Refund exceeds original sale amount"
            );
        }
    }

    private void applyGstRule(TransactionEntity txn, RuleEntity rule) {

        try {
            JsonNode node = mapper.readTree(rule.getConfigJson());

            if (node == null || node.get("amount") == null || node.get("minRate") == null) {
                throw new RuntimeException("Invalid GST rule configuration");
            }

            BigDecimal slabAmount = new BigDecimal(node.get("amount").asText());
            BigDecimal minRate = new BigDecimal(node.get("minRate").asText());

            // Null safety
            if (txn.getAmount() == null || txn.getTaxRate() == null) {
                return;
            }

            // Core logic
            if (txn.getAmount().compareTo(slabAmount) > 0 &&
                    txn.getTaxRate().compareTo(minRate) < 0) {

                exceptionService.create(
                        txn,
                        rule.getRuleName(),   // dynamic
                        "MEDIUM",
                        "GST slab violation: taxRate lower than expected"
                );
            }

        } catch (Exception e) {
            throw new RuntimeException("Error applying GST rule", e);
        }
    }
}