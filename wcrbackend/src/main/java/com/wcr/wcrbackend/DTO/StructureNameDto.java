package com.wcr.wcrbackend.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StructureNameDto {

    private String structureId;
    private String structureName;
    private String structureDetails;
    private BigDecimal fromChainage;   // DECIMAL(10,2)
    private BigDecimal toChainage;
}
