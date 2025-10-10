package com.wcr.wcrbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Messages {
	private String message_id,message,user_id_fk,redirect_url,created_date,read_time,message_type,created_date_24hr_format,timeAgo;
	
	private String[] user_ids,message_ids;
}
