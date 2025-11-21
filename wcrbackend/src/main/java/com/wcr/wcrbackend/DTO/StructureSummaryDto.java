package com.wcr.wcrbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StructureSummaryDto {
    private String structureType;
    private Long count;
}

