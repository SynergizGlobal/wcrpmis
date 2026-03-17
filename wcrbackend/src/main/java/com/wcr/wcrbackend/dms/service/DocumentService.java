package com.wcr.wcrbackend.dms.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.wcr.wcrbackend.dms.dto.DocumentDTO;
import com.wcr.wcrbackend.dms.dto.DocumentGridDTO;
import com.wcr.wcrbackend.dms.dto.MetaDataDto;
import com.wcr.wcrbackend.dms.dto.NotRequiredDTO;
import com.wcr.wcrbackend.dms.dto.SendDocumentDTO;
import com.wcr.wcrbackend.dms.entity.DocumentRevision;

public interface DocumentService {

    List<DocumentGridDTO> getAllDocuments();

    void saveDocument(DocumentDTO dto, MultipartFile file) throws Exception;

    ResponseEntity<Resource> downloadDocument(Long id) throws Exception;

    void updateDocument(Long id, DocumentDTO dto, MultipartFile file) throws Exception;

    public String saveOrSendDocument(SendDocumentDTO dto, String userId, String baseUrl);

    List<DocumentRevision> getDocumentVersions(String fileNumber);

    void sendDocument(SendDocumentDTO dto);

    public void markNotRequired(NotRequiredDTO notRequiredDto, String userId);

     public DocumentDTO uploadFileWithMetaData( DocumentDTO documentDto,
	    		List<MultipartFile> files, String userId) ;
	 
	 public List<Map<String, MetaDataDto>> validateMetadata(List<List<String>> rows, String userId, String userRoleName) throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException;
	 
}