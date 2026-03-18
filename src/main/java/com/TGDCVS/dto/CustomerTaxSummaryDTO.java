package com.TGDCVS.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTaxSummaryDTO {

    private String customerId;
    private BigDecimal totalAmount;
    private BigDecimal totalReportedTax;
    private BigDecimal totalExpectedTax;
    private BigDecimal totalTaxGap;
    private Double complianceScore;
}