package com.TGDCVS.serviceImpl;

import com.TGDCVS.dto.CustomerTaxSummaryDTO;
import com.TGDCVS.dto.ExceptionSummaryDTO;
import com.TGDCVS.repositories.ExceptionRepository;
import com.TGDCVS.repositories.TransactionRepository;
import com.TGDCVS.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final TransactionRepository txnRepo;
    private final ExceptionRepository exRepo;

    @Override
    public List<CustomerTaxSummaryDTO> getCustomerSummary() {

        List<Object[]> rows = txnRepo.getCustomerReport();
        List<CustomerTaxSummaryDTO> result = new ArrayList<>();

        for (Object[] r : rows) {

            String customerId = (String) r[0];
            BigDecimal totalAmount = (BigDecimal) r[1];
            BigDecimal totalReportedTax = (BigDecimal) r[2];
            BigDecimal totalExpectedTax = (BigDecimal) r[3];
            BigDecimal totalTaxGap = (BigDecimal) r[4];

            Long totalTxn = ((Number) r[5]).longValue();
            Long nonCompliant = ((Number) r[6]).longValue();

            double complianceScore =
                    100 - ((double) nonCompliant / totalTxn * 100);

            result.add(new CustomerTaxSummaryDTO(
                    customerId,
                    totalAmount,
                    totalReportedTax,
                    totalExpectedTax,
                    totalTaxGap,
                    complianceScore
            ));
        }

        return result;
    }

    @Override
    public ExceptionSummaryDTO getExceptionSummary() {
        return exRepo.getExceptionSummary();
    }

    @Override
    public Map<String, Long> getCustomerExceptionCount() {

        List<Object[]> rows = exRepo.customerWiseExceptions();
        Map<String, Long> map = new HashMap<>();

        for (Object[] r : rows) {
            map.put((String) r[0], ((Number) r[1]).longValue());
        }

        return map;
    }
}