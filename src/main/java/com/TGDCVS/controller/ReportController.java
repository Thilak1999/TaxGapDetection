package com.TGDCVS.controller;

import com.TGDCVS.dto.CustomerTaxSummaryDTO;
import com.TGDCVS.dto.ExceptionSummaryDTO;
import com.TGDCVS.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "Reporting APIs")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // A. Customer Summary
    @Operation(summary = "Get customer tax summary")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customers")
    public List<CustomerTaxSummaryDTO> getCustomerSummary() {
        return reportService.getCustomerSummary();
    }

    // B. Exception Summary
    @Operation(summary = "Get exception summary")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/exceptions/summary")
    public ExceptionSummaryDTO getExceptionSummary() {
        return reportService.getExceptionSummary();
    }

    // Customer-wise Exception Count
    @Operation(summary = "Get customer-wise exception count")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/exceptions/customer")
    public Map<String, Long> getCustomerExceptionCount() {
        return reportService.getCustomerExceptionCount();
    }
}