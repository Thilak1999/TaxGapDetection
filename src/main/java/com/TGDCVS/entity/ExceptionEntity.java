package com.TGDCVS.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "exceptions")
@Getter
@Setter
public class ExceptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String customerId;
    private String ruleName;
    private String severity;
    private String message;

    private LocalDateTime timestamp;
}