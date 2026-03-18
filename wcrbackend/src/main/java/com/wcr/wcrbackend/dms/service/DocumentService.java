package com.wcr.wcrbackend.dms.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.wcr.wcrbackend.dms.dto.DocumentDTO;
import com.wcr.wcrbackend.dms.dto.DocumentGridDTO;
import com.wcr.wcrbackend.dms.dto.DocumentRevisionDTO;
import com.wcr.wcrbackend.dms.dto.MetaDataDto;
import com.wcr.wcrbackend.dms.dto.NotRequiredDTO;
import com.wcr.wcrbackend.dms.dto.SaveMetaDataDto;
import com.wcr.wcrbackend.dms.dto.SendDocumentDTO;
import com.wcr.wcrbackend.dms.entity.DocumentRevision;
import com.wcr.wcrbackend.entity.User;

public interface DocumentService {

    List<DocumentGridDTO> getAllDocuments();

    void saveDocument(DocumentDTO dto, MultipartFile file) throws Exception;

    ResponseEntity<Resource> downloadDocument(Long id) throws Exception;

    void updateDocument(Long id, DocumentDTO dto, MultipartFile file) throws Exception;

    public String saveOrSendDocument(SendDocumentDTO dto, String userId, String baseUrl);

    List<DocumentRevisionDTO> getDocumentVersions(String fileNumber);

    void sendDocument(SendDocumentDTO dto);

    public void markNotRequired(NotRequiredDTO notRequiredDto, String userId);

	void saveMultipleDocuments(DocumentDTO dto, List<MultipartFile> files) throws Exception;

	public void processBulkZip(Long id, MultipartFile zipFile,
            Map<Long, List<DocumentDTO>> bulkStore) throws Exception;

     public DocumentDTO uploadFileWithMetaData( DocumentDTO documentDto,
	    		List<MultipartFile> files, String userId) ;
	 
	 public List<Map<String, MetaDataDto>> validateMetadata(List<List<String>> rows, String userId, String userRoleName) throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException;

    //  previous services
        // public String validateUploadDocument(String... args);

		// public String validateDepartment(String... args);

		// public String validateStatus(String... args);

		// public String validateSubFolder(String... args);

		// public String validateFolder(String... args);

		// public String validateRevisionDate(String... args);
		
		// public String validateRevisionNumber(String... args);

		// public String validateFileNumber(String... args);

		// public String validateFileName(String... args);

		// public Long saveMetadata(List<SaveMetaDataDto> dtoList, String userId);

		// public String saveZipFileAndCreateDocuments(Long uploadId, MultipartFile file, String userId);

		// public Long getMetadata(String string);

		// public List<String> findGroupedFileNames(String userId);

		// public List<String> findGroupedFileTypes(String userId);

		// public List<String> findGroupedFileNumbers(String string);

		// public List<String> findGroupedRevisionNos(String userId);
		
		// public List<String> findGroupedStatus(String userId);
		
		// public List<String> findGroupedFolders(String userId);
		
		// public List<String> findGroupedSubFolders(String userId);

		// public List<String> findGroupedUploadedDate(String userId);
		
		// public List<String> findGroupedRevisionDate(String userId);

		// public List<String> findGroupedDepartment(String userId);

		// public List<DocumentGridDTO> getFilteredDocuments(Map<Integer, List<String>> columnFilters, int start, int length, User user);

		// public long countFilteredDocuments(Map<Integer, List<String>> columnFilters, User user);

		// public List<String> findGroupedCreatedBy(String userId);

		// public List<String> findGroupedProjectNames(String userId);

		// public List<String> findGroupedContractNames(String userId);

		// public long countAllFiles(User user);

		// public String getFilePath(String fileName, String fileNumber, String revisionNo);


		// public List<String> findAllFileTypes();

		// public List<String> findAllFileNumbers();

		// public List<String> findAllFileNames();

		// public List<String> findAllRevisionNos();

		// public List<String> findAllStatus();

		// public List<String> findAllProjectNamesByDocument();

		// public List<String> findAllContractNamesByDocument();

		// public List<String> findAllFoldersByDocument();

		// public List<String> findAllSubFoldersByDocument();

		// public List<String> findAllCreatedByDocument();

		// public List<String> findAllRevisionDateByDocument();

		// public List<String> findAllDepartmentByDocument();

	 
}