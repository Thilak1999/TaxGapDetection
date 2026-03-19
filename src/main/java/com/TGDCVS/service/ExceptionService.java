package com.TGDCVS.service;

import com.TGDCVS.entity.ExceptionEntity;
import com.TGDCVS.entity.TransactionEntity;

import java.util.List;

public interface ExceptionService {

    void create(TransactionEntity txn, String ruleName, String severity, String message);

    List<ExceptionEntity> getAll();

    List<ExceptionEntity> search(String customerId, String severity, String ruleName);
}