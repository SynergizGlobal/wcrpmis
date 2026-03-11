package com.wcr.wcrbackend.dms.service;

import com.wcr.wcrbackend.dms.dto.DraftDataTableRequest;
import com.wcr.wcrbackend.dms.dto.DraftDataTableResponse;
import com.wcr.wcrbackend.dms.dto.DraftSendDocumentDTO;

public interface SendDocumentService {
    	public DraftDataTableResponse<DraftSendDocumentDTO> getDrafts(DraftDataTableRequest request, String userId);

}
