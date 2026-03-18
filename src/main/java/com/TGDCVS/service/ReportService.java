package com.TGDCVS.service;

import com.TGDCVS.dto.CustomerTaxSummaryDTO;
import com.TGDCVS.dto.ExceptionSummaryDTO;

import java.util.List;
import java.util.Map;

public interface ReportService {

    List<CustomerTaxSummaryDTO> getCustomerSummary();

    ExceptionSummaryDTO getExceptionSummary();

    Map<String, Long> getCustomerExceptionCount();
}