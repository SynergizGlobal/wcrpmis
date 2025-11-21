package com.wcr.wcrbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StructureTypeDto {
    private String type;
    private List<StructureNameDto> rows;

}
