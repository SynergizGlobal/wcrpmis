package com.wcr.wcrbackend.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StructureSaveRequest {
    private String project;
    private List<StructureTypeDto> structureTypes;
}

