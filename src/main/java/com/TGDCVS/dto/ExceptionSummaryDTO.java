package com.TGDCVS.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionSummaryDTO {

    private Long totalExceptions;
    private Long high;
    private Long medium;
    private Long low;
}