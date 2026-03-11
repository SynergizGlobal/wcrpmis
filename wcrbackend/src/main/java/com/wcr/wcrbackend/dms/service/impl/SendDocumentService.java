package com.wcr.wcrbackend.dms.service.impl;

import com.wcr.wcrbackend.dms.dto.DraftDataTableRequest;
import com.wcr.wcrbackend.dms.dto.DraftDataTableResponse;
import com.wcr.wcrbackend.dms.dto.DraftSendDocumentDTO;

interface SendDocumentService {

    DraftDataTableResponse<DraftSendDocumentDTO> getDrafts(DraftDataTableRequest request, String userId);

}
