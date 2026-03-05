package com.wcr.wcrbackend.dms.service;

import com.wcr.wcrbackend.dms.dto.DocumentDTO;
import com.wcr.wcrbackend.dms.dto.DocumentGridDTO;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    List<DocumentGridDTO> getAllDocuments();

    void saveDocument(DocumentDTO dto, MultipartFile file) throws Exception;

    ResponseEntity<Resource> downloadDocument(Long id) throws Exception;
}