package com.test.TGDCVS;

import com.TGDCVS.dto.TransactionDTO;
import com.TGDCVS.entity.TransactionEntity;
import com.TGDCVS.enums.ComplianceStatus;
import com.TGDCVS.enums.ValidationStatus;
import com.TGDCVS.repositories.TransactionRepository;
import com.TGDCVS.ruleengine.RuleEngineService;
import com.TGDCVS.serviceImpl.AuditService;
import com.TGDCVS.serviceImpl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private TransactionRepository repo;
    private RuleEngineService ruleEngine;
    private AuditService auditService;
    private TransactionServiceImpl service;

    @BeforeEach
    void setup() {
        repo = mock(TransactionRepository.class);
        ruleEngine = mock(RuleEngineService.class);
        auditService = mock(AuditService.class);

        service = new TransactionServiceImpl(repo, ruleEngine, auditService);
    }

    // SUCCESS FLOW
    @Test
    void testProcessTransactions_Success() {

        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId("TXN1");
        dto.setDate(LocalDate.now());
        dto.setCustomerId("C1");
        dto.setAmount(new BigDecimal("1000"));
        dto.setTaxRate(new BigDecimal("0.18"));
        dto.setReportedTax(new BigDecimal("180"));

        service.processTransactions(List.of(dto));

        // Capture saved entity
        ArgumentCaptor<TransactionEntity> captor =
                ArgumentCaptor.forClass(TransactionEntity.class);

        verify(repo).save(captor.capture());

        TransactionEntity saved = captor.getValue();

        assertEquals(ValidationStatus.SUCCESS, saved.getValidationStatus());
        assertEquals(ComplianceStatus.COMPLIANT, saved.getComplianceStatus());

        verify(ruleEngine).applyRules(any());
        verify(auditService).log(eq("INGESTION"), eq("TXN1"), any());
    }

    // VALIDATION FAILURE
    @Test
    void testProcessTransactions_ValidationFailure() {

        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId(null); // invalid
        dto.setAmount(new BigDecimal("-10")); // invalid

        service.processTransactions(List.of(dto));

        ArgumentCaptor<TransactionEntity> captor =
                ArgumentCaptor.forClass(TransactionEntity.class);

        verify(repo).save(captor.capture());

        TransactionEntity saved = captor.getValue();

        assertEquals(ValidationStatus.FAILURE, saved.getValidationStatus());
        assertTrue(saved.getFailureReason().contains("Invalid amount"));
        assertTrue(saved.getFailureReason().contains("Missing transactionId"));

        verify(ruleEngine, never()).applyRules(any());
        verify(auditService, never()).log(any(), any(), any());
    }

    // NON-COMPLIANT (reportedTax null)
    @Test
    void testCalculateTax_NonCompliant() {

        TransactionEntity entity = new TransactionEntity();
        entity.setTransactionId("TXN2");
        entity.setAmount(new BigDecimal("1000"));
        entity.setTaxRate(new BigDecimal("0.18"));
        entity.setReportedTax(null);

        service.calculateTax(entity);

        assertEquals(ComplianceStatus.NON_COMPLIANT, entity.getComplianceStatus());

        verify(auditService, never()).log(eq("TAX_COMPUTATION"), any(), any());
    }

    // UNDERPAID
    @Test
    void testCalculateTax_Underpaid() {

        TransactionEntity entity = new TransactionEntity();
        entity.setTransactionId("TXN3");
        entity.setAmount(new BigDecimal("1000"));
        entity.setTaxRate(new BigDecimal("0.18"));
        entity.setReportedTax(new BigDecimal("100"));

        service.calculateTax(entity);

        assertEquals(ComplianceStatus.UNDERPAID, entity.getComplianceStatus());
        verify(auditService).log(eq("TAX_COMPUTATION"), eq("TXN3"), any());
    }

    // OVERPAID
    @Test
    void testCalculateTax_Overpaid() {

        TransactionEntity entity = new TransactionEntity();
        entity.setTransactionId("TXN4");
        entity.setAmount(new BigDecimal("1000"));
        entity.setTaxRate(new BigDecimal("0.18"));
        entity.setReportedTax(new BigDecimal("250"));

        service.calculateTax(entity);

        assertEquals(ComplianceStatus.OVERPAID, entity.getComplianceStatus());
    }

    // COMPLIANT (gap <= 1)
    @Test
    void testCalculateTax_Compliant() {

        TransactionEntity entity = new TransactionEntity();
        entity.setTransactionId("TXN5");
        entity.setAmount(new BigDecimal("1000"));
        entity.setTaxRate(new BigDecimal("0.18"));
        entity.setReportedTax(new BigDecimal("179"));

        service.calculateTax(entity);

        assertEquals(ComplianceStatus.COMPLIANT, entity.getComplianceStatus());
    }
}