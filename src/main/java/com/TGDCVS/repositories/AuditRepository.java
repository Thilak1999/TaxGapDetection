package com.TGDCVS.repositories;

import com.TGDCVS.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<AuditLog, Long> {
}