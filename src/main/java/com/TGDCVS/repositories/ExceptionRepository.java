package com.TGDCVS.repositories;

import com.TGDCVS.dto.ExceptionSummaryDTO;
import com.TGDCVS.entity.ExceptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExceptionRepository extends JpaRepository<ExceptionEntity, Long> {


    @Query(value = """
SELECT customer_id, COUNT(*) 
FROM exceptions 
GROUP BY customer_id
""", nativeQuery = true)
    List<Object[]> customerWiseExceptions();

    @Query("""
    SELECT new com.TGDCVS.dto.ExceptionSummaryDTO(
        COUNT(e),
        SUM(CASE WHEN e.severity = 'HIGH' THEN 1 ELSE 0 END),
        SUM(CASE WHEN e.severity = 'MEDIUM' THEN 1 ELSE 0 END),
        SUM(CASE WHEN e.severity = 'LOW' THEN 1 ELSE 0 END)
    )
    FROM ExceptionEntity e
""")
    ExceptionSummaryDTO getExceptionSummary();
    List<ExceptionEntity> findByCustomerId(String customerId);

    List<ExceptionEntity> findBySeverity(String severity);

    List<ExceptionEntity> findByRuleName(String ruleName);

}