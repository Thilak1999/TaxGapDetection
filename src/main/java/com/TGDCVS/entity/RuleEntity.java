package com.TGDCVS.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rules")
@Getter
@Setter
public class RuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleName;
    private String ruleType;

    @Column(columnDefinition = "TEXT")
    private String configJson;

    private boolean enabled;
}