package com.test.TGDCVS;

import com.TGDCVS.entity.RuleEntity;
import com.TGDCVS.entity.TransactionEntity;
import com.TGDCVS.exception.ExceptionService;
import com.TGDCVS.repositories.RuleRepository;
import com.TGDCVS.ruleengine.RuleEngineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

class RuleEngineServiceTest {

    @Test
    void testHighValueRuleTriggered() throws Exception {

        // Mock dependencies
        RuleRepository ruleRepo = mock(RuleRepository.class);
        ExceptionService exceptionService = mock(ExceptionService.class);
        ObjectMapper mapper = new ObjectMapper();

        RuleEngineService ruleEngine =
                new RuleEngineService(ruleRepo, exceptionService, mapper);

        // Prepare rule
        RuleEntity rule = new RuleEntity();
        rule.setRuleName("HIGH_VALUE");
        rule.setEnabled(true);
        rule.setConfigJson("{\"threshold\":50000}");

        when(ruleRepo.findByEnabledTrue()).thenReturn(List.of(rule));

        // Prepare transaction
        TransactionEntity txn = new TransactionEntity();
        txn.setAmount(new BigDecimal("100000"));

        // Execute
        ruleEngine.applyRules(txn);

        // Verify exception created
        verify(exceptionService, times(1))
                .create(txn, "HIGH_VALUE", "HIGH", "High value transaction");
    }
    @Test
    void testHighValueRuleNotTriggered() {

        RuleRepository ruleRepo = mock(RuleRepository.class);
        ExceptionService exceptionService = mock(ExceptionService.class);
        ObjectMapper mapper = new ObjectMapper();

        RuleEngineService ruleEngine =
                new RuleEngineService(ruleRepo, exceptionService, mapper);

        RuleEntity rule = new RuleEntity();
        rule.setRuleName("HIGH_VALUE");
        rule.setEnabled(true);
        rule.setConfigJson("{\"threshold\":50000}");

        when(ruleRepo.findByEnabledTrue()).thenReturn(List.of(rule));

        TransactionEntity txn = new TransactionEntity();
        txn.setAmount(new BigDecimal("1000"));

        ruleEngine.applyRules(txn);

        // No exception should be created
        verify(exceptionService, never())
                .create(any(), any(), any(), any());
    }
}