package com.wcr.dms.service;

import java.util.List;

import com.wcr.dms.dto.ReferenceLetterDTO;



public interface CorrespondenceReferenceService {
	 
	 List<ReferenceLetterDTO> getReferenceLettersByCorrespondenceId(Long correspondenceId);
	 
	 
 }