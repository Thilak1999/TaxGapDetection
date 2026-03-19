package com.test.TGDCVS;

import com.TGDCVS.entity.ExceptionEntity;
import com.TGDCVS.entity.TransactionEntity;
import com.TGDCVS.repositories.ExceptionRepository;
import com.TGDCVS.serviceImpl.ExceptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ExceptionServiceTest {

    private ExceptionRepository repo;
    private ExceptionServiceImpl service;

    @BeforeEach
    void setup() {
        repo = mock(ExceptionRepository.class);
        service = new ExceptionServiceImpl(repo);
    }

    @Test
    void testCreateException() {

        TransactionEntity txn = new TransactionEntity();
        txn.setTransactionId("TXN1");
        txn.setCustomerId("CUST1");

        service.create(txn, "HIGH_VALUE", "HIGH", "High value transaction");

        verify(repo, times(1)).save(any(ExceptionEntity.class));
    }

    @Test
    void testGetAllExceptions() {

        when(repo.findAll()).thenReturn(List.of(new ExceptionEntity()));

        List<ExceptionEntity> result = service.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void testSearchByCustomerId() {

        when(repo.findByCustomerId("CUST1"))
                .thenReturn(List.of(new ExceptionEntity()));

        List<ExceptionEntity> result =
                service.search("CUST1", null, null);

        assertEquals(1, result.size());
        verify(repo).findByCustomerId("CUST1");
    }

    @Test
    void testSearchBySeverity() {

        when(repo.findBySeverity("HIGH"))
                .thenReturn(List.of(new ExceptionEntity()));

        List<ExceptionEntity> result =
                service.search(null, "HIGH", null);

        assertEquals(1, result.size());
        verify(repo).findBySeverity("HIGH");
    }

    @Test
    void testSearchByRuleName() {

        when(repo.findByRuleName("HIGH_VALUE"))
                .thenReturn(List.of(new ExceptionEntity()));

        List<ExceptionEntity> result =
                service.search(null, null, "HIGH_VALUE");

        assertEquals(1, result.size());
        verify(repo).findByRuleName("HIGH_VALUE");
    }

    @Test
    void testSearchNoFilter() {

        when(repo.findAll()).thenReturn(List.of(new ExceptionEntity()));

        List<ExceptionEntity> result =
                service.search(null, null, null);

        assertEquals(1, result.size());
        verify(repo).findAll();
    }



}