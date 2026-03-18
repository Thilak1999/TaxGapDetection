package com.TGDCVS.entity;

import com.TGDCVS.enums.ComplianceStatus;
import com.TGDCVS.enums.TransactionType;
import com.TGDCVS.enums.ValidationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private LocalDate date;
    private String customerId;

    private BigDecimal amount;
    private BigDecimal taxRate;
    private BigDecimal reportedTax;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private ValidationStatus validationStatus;

    private String failureReason;

    private BigDecimal expectedTax;
    private BigDecimal taxGap;

    @Enumerated(EnumType.STRING)
    private ComplianceStatus complianceStatus;
}