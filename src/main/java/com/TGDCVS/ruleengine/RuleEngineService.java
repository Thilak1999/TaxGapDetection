package com.TGDCVS.ruleengine;

import com.TGDCVS.entity.RuleEntity;
import com.TGDCVS.entity.TransactionEntity;
import com.TGDCVS.exception.ExceptionService;
import com.TGDCVS.repositories.RuleRepository;
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
    private final ObjectMapper mapper;

    public void applyRules(TransactionEntity txn) {

        List<RuleEntity> rules = ruleRepo.findByEnabledTrue();

        for (RuleEntity rule : rules) {

            switch (rule.getRuleName()) {

                case "HIGH_VALUE":
                    applyHighValue(txn, rule);
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
}