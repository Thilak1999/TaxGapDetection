package com.TGDCVS.repositories;

import com.TGDCVS.entity.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RuleRepository extends JpaRepository<RuleEntity, Long> {
    List<RuleEntity> findByEnabledTrue();

}