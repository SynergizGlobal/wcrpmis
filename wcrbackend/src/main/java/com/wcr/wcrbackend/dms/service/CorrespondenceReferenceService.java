package com.wcr.wcrbackend.dms.service;

import java.util.List;

import com.wcr.wcrbackend.dms.dto.ReferenceLetterDTO;



public interface CorrespondenceReferenceService {
	 
	 List<ReferenceLetterDTO> getReferenceLettersByCorrespondenceId(Long correspondenceId);
	 
	 
 }