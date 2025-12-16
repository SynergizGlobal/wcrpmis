package com.wcr.wcrbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Insurence {
	private String contract_id_fk, insurance_type_fk, issuing_agency, agency_address, insurance_number, insurance_value, valid_upto, remarks;
}
	