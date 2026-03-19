package com.TGDCVS.controller;

import com.TGDCVS.entity.ExceptionEntity;
import com.TGDCVS.service.ExceptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exceptions")
@RequiredArgsConstructor
@Tag(name = "Exceptions APIs")
public class ExceptionController {

    private final ExceptionService service;

    @Operation(summary = "Get all exceptions")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<ExceptionEntity> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Search exceptions")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public List<ExceptionEntity> search(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String ruleName) {

        return service.search(customerId, severity, ruleName);
    }
}