package com.TGDCVS.repositories;

import com.TGDCVS.entity.TransactionEntity;
import com.TGDCVS.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query(value = """
SELECT 
    customer_id,
    SUM(amount) as totalAmount,
    SUM(reported_tax) as totalReportedTax,
    SUM(expected_tax) as totalExpectedTax,
    SUM(tax_gap) as totalTaxGap,
    COUNT(*) as totalTxn,
    SUM(CASE WHEN compliance_status = 'NON_COMPLIANT' THEN 1 ELSE 0 END) as nonCompliant
FROM transactions
GROUP BY customer_id
""", nativeQuery = true)
    List<Object[]> getCustomerReport();

    TransactionEntity findTopByCustomerIdAndTransactionType(String customerId, TransactionType transactionType);

    Optional<TransactionEntity> findTopByCustomerIdAndTransactionTypeOrderByDateDesc(
            String customerId,
            TransactionType transactionType
    );
}