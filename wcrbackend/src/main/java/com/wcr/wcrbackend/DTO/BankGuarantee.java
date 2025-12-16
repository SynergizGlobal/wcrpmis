package com.wcr.wcrbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankGuarantee {
	private String contract_id_fk,bg_type_fk,issuing_bank,bank_address,bg_number,bg_value,valid_upto,remarks;
}