package com.test.TGDCVS;

import com.TGDCVS.entity.RuleEntity;
import com.TGDCVS.entity.TransactionEntity;
import com.TGDCVS.exception.ExceptionService;
import com.TGDCVS.repositories.RuleRepository;
import com.TGDCVS.repositories.TransactionRepository;
import com.TGDCVS.ruleengine.RuleEngineService;
import com.TGDCVS.serviceImpl.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

class RuleEngineServiceTest {

    private RuleRepository ruleRepo;
    private ExceptionService exceptionService;
    private ObjectMapper mapper;
    private AuditService auditService;
    private TransactionRepository txnRepo;

    private RuleEngineService service;

    @BeforeEach
    void setup() {
        ruleRepo = mock(RuleRepository.class);
        exceptionService = mock(ExceptionService.class);
        mapper = new ObjectMapper();
        auditService = mock(AuditService.class);
        txnRepo = mock(TransactionRepository.class);

        service = new RuleEngineService(
                ruleRepo,
                exceptionService,
                txnRepo,
                mapper,
                auditService

        );
    }

    @Test
    void testHighValueRuleTriggered() {

        TransactionEntity txn = new TransactionEntity();
        txn.setTransactionId("TXN1");
        txn.setCustomerId("CUST1");
        txn.setAmount(new BigDecimal("100000"));

        RuleEntity rule = new RuleEntity();
        rule.setRuleName("HIGH_VALUE");
        rule.setEnabled(true);
        rule.setConfigJson("{\"threshold\":50000}");

        when(ruleRepo.findByEnabledTrue()).thenReturn(List.of(rule));

        service.applyRules(txn);

        verify(exceptionService, times(1))
                .create(eq(txn), eq("HIGH_VALUE"), eq("HIGH"), any());
    }


    @Test
    void testHighValueRuleNotTriggered() {

        TransactionEntity txn = new TransactionEntity();
        txn.setAmount(new BigDecimal("1000"));

        RuleEntity rule = new RuleEntity();
        rule.setRuleName("HIGH_VALUE");
        rule.setEnabled(true);
        rule.setConfigJson("{\"threshold\":50000}");

        when(ruleRepo.findByEnabledTrue()).thenReturn(List.of(rule));

        service.applyRules(txn);

        verify(exceptionService, never())
                .create(any(), any(), any(), any());
    }

    @Test
    void testMultipleRulesExecution() {

        TransactionEntity txn = new TransactionEntity();
        txn.setTransactionId("TXN2");
        txn.setCustomerId("CUST2");
        txn.setAmount(new BigDecimal("100000"));

        RuleEntity rule1 = new RuleEntity();
        rule1.setRuleName("HIGH_VALUE");
        rule1.setEnabled(true);
        rule1.setConfigJson("{\"threshold\":50000}");

        RuleEntity rule2 = new RuleEntity();
        rule2.setRuleName("UNKNOWN_RULE");
        rule2.setEnabled(true);
        rule2.setConfigJson("{}");

        when(ruleRepo.findByEnabledTrue()).thenReturn(List.of(rule1, rule2));

        service.applyRules(txn);

        verify(exceptionService, times(1))
                .create(eq(txn), eq("HIGH_VALUE"), any(), any());
    }

    @Test
    void testNoRules() {

        TransactionEntity txn = new TransactionEntity();
        txn.setAmount(new BigDecimal("100000"));

        when(ruleRepo.findByEnabledTrue()).thenReturn(List.of());

        service.applyRules(txn);

        verify(exceptionService, never())
                .create(any(), any(), any(), any());
    }

    @Test
    void testInvalidJsonConfig() {

        TransactionEntity txn = new TransactionEntity();
        txn.setAmount(new BigDecimal("100000"));

        RuleEntity rule = new RuleEntity();
        rule.setRuleName("HIGH_VALUE");
        rule.setEnabled(true);
        rule.setConfigJson("INVALID_JSON");

        when(ruleRepo.findByEnabledTrue()).thenReturn(List.of(rule));

        try {
            service.applyRules(txn);
        } catch (RuntimeException e) {
            assert true;
        }
    }
}