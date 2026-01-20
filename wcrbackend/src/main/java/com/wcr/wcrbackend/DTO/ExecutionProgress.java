package com.wcr.wcrbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionProgress {
	private String project;
    private double projectFromKm;
    private double projectToKm;
    private String contract;
    private String contract_id;
    private String contract_name;
    private String contractor;
    private String subStructure;
    private double fromKm;
    private double toKm;
    private String status;
    private double progress;
    private String barColor;
    private String barLabel;
    private String structureType;

}
