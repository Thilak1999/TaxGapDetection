package com.TGDCVS.dto;

import com.TGDCVS.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransactionDTO {

    private String transactionId;
    private LocalDate date;
    private String customerId;

    private BigDecimal amount;
    private BigDecimal taxRate;
    private BigDecimal reportedTax;

    private TransactionType transactionType;
}