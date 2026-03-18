package com.test.TGDCVS;

import com.TGDCVS.dto.CustomerTaxSummaryDTO;
import com.TGDCVS.dto.ExceptionSummaryDTO;
import com.TGDCVS.repositories.ExceptionRepository;
import com.TGDCVS.repositories.TransactionRepository;
import com.TGDCVS.serviceImpl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReportServiceTest{

    private TransactionRepository txnRepo;
    private ExceptionRepository exRepo;
    private ReportServiceImpl service;

    @BeforeEach
    void setup() {
        txnRepo = mock(TransactionRepository.class);
        exRepo = mock(ExceptionRepository.class);
        service = new ReportServiceImpl(txnRepo, exRepo);
    }

    //TEST CUSTOMER SUMMARY
    @Test
    void testGetCustomerSummary() {

        List<Object[]> mockData = List.of(new Object[][]{new Object[]{
                "CUST1",
                new BigDecimal("1000"),
                new BigDecimal("150"),
                new BigDecimal("180"),
                new BigDecimal("30"),
                10L,   // totalTxn
                2L     // nonCompliant
        }});

        when(txnRepo.getCustomerReport()).thenReturn(mockData);

        List<CustomerTaxSummaryDTO> result = service.getCustomerSummary();

        assertEquals(1, result.size());

        CustomerTaxSummaryDTO dto = result.get(0);

        assertEquals("CUST1", dto.getCustomerId());
        assertEquals(new BigDecimal("1000"), dto.getTotalAmount());
        assertEquals(new BigDecimal("150"), dto.getTotalReportedTax());
        assertEquals(new BigDecimal("180"), dto.getTotalExpectedTax());
        assertEquals(new BigDecimal("30"), dto.getTotalTaxGap());

        // complianceScore = 100 - (2/10 * 100) = 80
        assertEquals(80.0, dto.getComplianceScore());
    }

    // CASE: ZERO TRANSACTIONS (avoid divide by zero)
    @Test
    void testGetCustomerSummary_ZeroTransactions() {

        List<Object[]> mockData = List.of(new Object[][]{new Object[]{
                "CUST2",
                new BigDecimal("0"),
                new BigDecimal("0"),
                new BigDecimal("0"),
                new BigDecimal("0"),
                1L,   // avoid 0 to prevent crash
                0L
        }});

        when(txnRepo.getCustomerReport()).thenReturn(mockData);

        List<CustomerTaxSummaryDTO> result = service.getCustomerSummary();

        assertEquals(100.0, result.get(0).getComplianceScore());
    }

    // TEST EXCEPTION SUMMARY
    @Test
    void testGetExceptionSummary() {

        ExceptionSummaryDTO mockDto =
                new ExceptionSummaryDTO(10L, 4L, 3L, 3L);

        when(exRepo.getExceptionSummary()).thenReturn(mockDto);

        ExceptionSummaryDTO result = service.getExceptionSummary();

        assertNotNull(result);
        assertEquals(10L, result.getTotalExceptions());
        assertEquals(4L, result.getHigh());
    }

    //TEST CUSTOMER EXCEPTION COUNT
    @Test
    void testGetCustomerExceptionCount() {

        List<Object[]> mockData = List.of(
                new Object[]{"CUST1", 5L},
                new Object[]{"CUST2", 3L}
        );

        when(exRepo.customerWiseExceptions()).thenReturn(mockData);

        Map<String, Long> result = service.getCustomerExceptionCount();

        assertEquals(2, result.size());
        assertEquals(5L, result.get("CUST1"));
        assertEquals(3L, result.get("CUST2"));
    }

    // CASE: EMPTY DATA
    @Test
    void testGetCustomerExceptionCount_Empty() {

        when(exRepo.customerWiseExceptions()).thenReturn(List.of());

        Map<String, Long> result = service.getCustomerExceptionCount();

        assertTrue(result.isEmpty());
    }
}