package com.test.TGDCVS;

import com.TGDCVS.entity.TransactionEntity;
import com.TGDCVS.enums.ComplianceStatus;
import com.TGDCVS.repositories.TransactionRepository;
import com.TGDCVS.ruleengine.RuleEngineService;
import com.TGDCVS.serviceImpl.AuditService;
import com.TGDCVS.serviceImpl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaxTest {

    @Test
    void testTaxCalculation() {

        TransactionRepository repo = Mockito.mock(TransactionRepository.class);
        RuleEngineService ruleEngine = Mockito.mock(RuleEngineService.class);
        AuditService auditService = Mockito.mock(AuditService.class);

        TransactionServiceImpl service =
                new TransactionServiceImpl(repo, ruleEngine, auditService);

        TransactionEntity txn = new TransactionEntity();
        txn.setAmount(new BigDecimal("100"));
        txn.setTaxRate(new BigDecimal("0.1"));
        txn.setReportedTax(new BigDecimal("10"));

        service.calculateTax(txn);

        assertEquals(ComplianceStatus.COMPLIANT, txn.getComplianceStatus());
    }
}