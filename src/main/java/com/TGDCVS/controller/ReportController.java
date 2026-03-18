package com.TGDCVS.controller;

import com.TGDCVS.dto.CustomerTaxSummaryDTO;
import com.TGDCVS.dto.ExceptionSummaryDTO;
import com.TGDCVS.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // A. Customer Summary
    @GetMapping("/customers")
    public List<CustomerTaxSummaryDTO> getCustomerSummary() {
        return reportService.getCustomerSummary();
    }

    // B. Exception Summary
    @GetMapping("/exceptions/summary")
    public ExceptionSummaryDTO getExceptionSummary() {
        return reportService.getExceptionSummary();
    }

    // Customer-wise Exception Count
    @GetMapping("/exceptions/customer")
    public Map<String, Long> getCustomerExceptionCount() {
        return reportService.getCustomerExceptionCount();
    }
}