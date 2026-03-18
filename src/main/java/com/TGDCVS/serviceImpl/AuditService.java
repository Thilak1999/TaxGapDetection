package com.TGDCVS.serviceImpl;

import com.TGDCVS.entity.AuditLog;
import com.TGDCVS.repositories.AuditRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository repo;
    private final ObjectMapper mapper;

    public void log(String event, String txnId, Object detail) {

        try {
            AuditLog log = new AuditLog();
            log.setEventType(event);
            log.setTransactionId(txnId);
            log.setTimestamp(LocalDateTime.now());
            log.setDetailJson(mapper.writeValueAsString(detail));

            repo.save(log);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}