package com.wcr.wcrbackend.dms.service.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.wcr.wcrbackend.dms.constant.Constant;
import com.wcr.wcrbackend.dms.dto.DocumentDTO;
import com.wcr.wcrbackend.dms.dto.DocumentGridDTO;
import com.wcr.wcrbackend.dms.dto.DocumentRevisionDTO;
import com.wcr.wcrbackend.dms.dto.MetaDataDto;
import com.wcr.wcrbackend.dms.dto.NotRequiredDTO;
import com.wcr.wcrbackend.dms.dto.SaveMetaDataDto;
import com.wcr.wcrbackend.dms.dto.SendDocumentDTO;
import com.wcr.wcrbackend.dms.entity.Department;
import com.wcr.wcrbackend.dms.entity.Document;
import com.wcr.wcrbackend.dms.entity.DocumentFile;
import com.wcr.wcrbackend.dms.entity.DocumentRevision;
import com.wcr.wcrbackend.dms.entity.Folder;
import com.wcr.wcrbackend.dms.entity.SendDocument;
import com.wcr.wcrbackend.dms.entity.Status;
import com.wcr.wcrbackend.dms.entity.SubFolder;
import com.wcr.wcrbackend.dms.repository.DepartmentRepository;
import com.wcr.wcrbackend.dms.repository.DocumentFileRepository;
import com.wcr.wcrbackend.dms.repository.DocumentRepository;
import com.wcr.wcrbackend.dms.repository.DocumentRevisionRepository;
import com.wcr.wcrbackend.dms.repository.FolderRepository;
import com.wcr.wcrbackend.dms.repository.SendDocumentRepository;
import com.wcr.wcrbackend.dms.repository.StatusRepository;
import com.wcr.wcrbackend.dms.repository.SubFolderRepository;
import com.wcr.wcrbackend.dms.service.DocumentService;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.repo.UserDao;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    @Qualifier("dmsDocumentRepository")
    private final DocumentRepository documentRepository;
    private final DocumentFileRepository documentFileRepository;
    private final DocumentRevisionRepository documentRevisionRepository;
    private final FolderRepository folderRepository;
    private final SubFolderRepository subFolderRepository;
    private final DepartmentRepository departmentRepository;
    private final StatusRepository statusRepository;
	// private SendDocumentRepository sendDocumentRepository;
    private final UserDao userRepository;
    private DocumentService documentService;
    private final SendDocumentRepository sendDocumentRepository;

    @Value("${file.upload-dir}")
	private String basePath;

	@Value("${file.zip-dir}")
	private String zipPath;

	@Value("${spring.mail.host}")
	private String emailHost;

	@Value("${spring.mail.username}")
	private String emailUserName;

	@Value("${spring.mail.password}")
	private String emailPassword;


    private final String uploadDir = "uploads/";

    @Override
    public List<DocumentGridDTO> getAllDocuments() {

        List<Document> docs = documentRepository.findActiveDocuments();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return docs.stream().map(d -> {

            DocumentGridDTO dto = new DocumentGridDTO();

            dto.setId(String.valueOf(d.getId()));
            dto.setFileName(d.getFileName());
            dto.setFileNumber(d.getFileNumber());

            dto.setRevisionNumber(
                d.getRevisionNo() != null ? d.getRevisionNo() : ""
            );

            dto.setRevisionDate(
                d.getRevisionDate() != null ? d.getRevisionDate().toString() : ""
            );

            dto.setProjectName(d.getProjectName());
            dto.setContractName(d.getContractName());

            dto.setPath(d.getPath() != null ? d.getPath() : "");

            dto.setDepartment(
                d.getDepartment() != null ? d.getDepartment().getName() : ""
            );

            dto.setStatus(
                d.getCurrentStatus() != null ? d.getCurrentStatus().getName() : ""
            );

            dto.setCreatedBy(d.getCreatedBy());

            dto.setDateUploaded(
                d.getCreatedAt() != null ? d.getCreatedAt().format(formatter) : ""
            );

            dto.setCreatedAt(
                d.getCreatedAt() != null ? d.getCreatedAt().format(formatter) : ""
            );

            dto.setUpdatedAt(
                d.getUpdatedAt() != null ? d.getUpdatedAt().format(formatter) : ""
            );

            if (d.getDocumentFiles() != null && !d.getDocumentFiles().isEmpty()) {
                dto.setFileType(d.getDocumentFiles().get(0).getFileType());
            }

            return dto;

        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveDocument(DocumentDTO dto, MultipartFile file) throws Exception {

        Document document = new Document();

        document.setFileName(dto.getFileName());
        document.setFileNumber(dto.getFileNumber());
        document.setRevisionNo(dto.getRevisionNo());
        document.setRevisionDate(dto.getRevisionDate());

        document.setProjectName(dto.getProjectName());
        document.setContractName(dto.getContractName());

        document.setPath(
            dto.getPath() != null ? dto.getPath().trim() : ""
        );

        if (dto.getDepartment() != null && !dto.getDepartment().trim().isEmpty()) {

            Long deptId = Long.parseLong(dto.getDepartment().trim());

            Department department = departmentRepository
                    .findById(deptId)
                    .orElse(null);

            document.setDepartment(department);
        }

        if (dto.getCurrentStatus() != null && !dto.getCurrentStatus().trim().isEmpty()) {

            Long statusId = Long.parseLong(dto.getCurrentStatus().trim());

            Status status = statusRepository
                    .findById(statusId)
                    .orElse(null);

            document.setCurrentStatus(status);
        }

        document.setCreatedBy("SYSTEM");
        document.setCreatedAt(LocalDateTime.now());

        // document = documentRepository.save(document);

        Folder folder = getOrCreateFolderByPath(dto.getPath());

            if (folder != null) {
                document.setFolder(folder);
            }

        document = documentRepository.save(document);

        String storedFileName = null;
        
        if (file != null && !file.isEmpty()) {

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            storedFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(storedFileName);

            Files.copy(file.getInputStream(), filePath);

            DocumentFile docFile = new DocumentFile();

            docFile.setFileName(storedFileName);
            docFile.setFileType(file.getContentType());
            docFile.setFilePath(filePath.toString());
            docFile.setDocument(document);

            documentFileRepository.save(docFile);
        }

        DocumentRevision revision = new DocumentRevision();

        revision.setFileName(dto.getFileName());
        revision.setFileNumber(dto.getFileNumber());
        revision.setRevisionNo(dto.getRevisionNo());
        revision.setRevisionDate(dto.getRevisionDate());
        revision.setDocument(document);

        documentRevisionRepository.save(revision);
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(Long id) throws Exception {

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (document.getDocumentFiles() == null || document.getDocumentFiles().isEmpty()) {
            throw new RuntimeException("No file attached to this document");
        }

        DocumentFile file = document.getDocumentFiles().get(0);

        Path path = Paths.get(file.getFilePath());

        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            throw new RuntimeException("File not found in storage");
        }

        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }

    private String buildFolderPath(Folder folder) {

        if (folder == null) return "";

        List<String> parts = new ArrayList<>();
        Folder current = folder;

        while (current != null) {

            parts.add(0, current.getName().trim()); 

            if (current.getParentId() != null) {
                current = folderRepository.findById(current.getParentId()).orElse(null);
            } else {
                current = null;
            }
        }

        return String.join("/", parts); 
    }

    private Folder getOrCreateFolderByPath(String path) {

        if (path == null || path.trim().isEmpty()) return null;

        String[] parts = path.split("/");
        Folder parent = null;

        for (String name : parts) {

            String trimmed = name.trim();
            if (trimmed.isEmpty()) continue;

            Long parentId = (parent != null) ? parent.getId() : null;

            Folder existing = folderRepository
                    .findByNameAndParentId(trimmed, parentId)
                    .orElse(null);

            if (existing == null) {

                Folder newFolder = new Folder();
                newFolder.setName(trimmed);
                newFolder.setParentId(parentId);

                existing = folderRepository.save(newFolder);
            }

            parent = existing;
        }

        return parent;
    }

    @Override
@Transactional
public DocumentDTO uploadFileWithMetaData(DocumentDTO documentDto, List<MultipartFile> files, String userId) {

    String path = documentDto.getPath();

    Department department = departmentRepository.findByName(documentDto.getDepartment()).get();
    Status status = statusRepository.findByName(documentDto.getCurrentStatus()).get();
    User user = userRepository.findById(userId).get();

    Optional<Document> documentInDBOptional = documentRepository.findByFileName(documentDto.getFileName());

    
    if (documentInDBOptional.isPresent()) {

        Document document = documentInDBOptional.get();

        if (!document.getFileNumber().equals(documentDto.getFileNumber())) {

            return DocumentDTO.builder()
                    .fileName(document.getFileName())
                    .fileNumber(document.getFileNumber())
                    .revisionNo(document.getRevisionNo())
                    .revisionDate(document.getRevisionDate())
                    .path(document.getPath())
                    .department(department.getName())
                    .currentStatus(status.getName())
                    .errorMessage("File name already exists with File number: "
                            + document.getFileNumber())
                    .projectName(document.getProjectName())
                    .contractName(document.getContractName())
                    .build();
        }
    }

    documentInDBOptional = documentRepository.findByFileNumber(documentDto.getFileNumber());

    
    if (documentInDBOptional.isPresent()) {

        Document document = documentInDBOptional.get();

        if (!document.getFileName().equals(documentDto.getFileName())) {

            return DocumentDTO.builder()
                    .fileName(document.getFileName())
                    .fileNumber(document.getFileNumber())
                    .revisionNo(document.getRevisionNo())
                    .revisionDate(document.getRevisionDate())
                    .path(document.getPath())
                    .department(department.getName())
                    .currentStatus(status.getName())
                    .errorMessage("File number already exists for File name: "
                            + document.getFileName())
                    .projectName(document.getProjectName())
                    .contractName(document.getContractName())
                    .build();
        }
    }

    documentInDBOptional = documentRepository
            .findByFileNameAndFileNumber(documentDto.getFileName(), documentDto.getFileNumber());

    // 3️⃣ Revision validation
    if (documentInDBOptional.isPresent()) {

        Document document = documentInDBOptional.get();

        if (isSmaller(documentDto.getRevisionNo(), document.getRevisionNo())) {

            return DocumentDTO.builder()
                    .fileName(document.getFileName())
                    .fileNumber(document.getFileNumber())
                    .revisionNo(document.getRevisionNo())
                    .revisionDate(document.getRevisionDate())
                    .path(document.getPath())
                    .department(department.getName())
                    .currentStatus(status.getName())
                    .errorMessage("Revision number must be greater than "
                            + document.getRevisionNo())
                    .projectName(document.getProjectName())
                    .contractName(document.getContractName())
                    .build();
        }
    }

    // 🔄 UPDATE EXISTING DOCUMENT
    if (documentInDBOptional.isPresent()) {

        Document documentInDB = documentInDBOptional.get();

        List<DocumentFile> archivedFiles;

        try {
            archivedFiles = moveFilesToArchiveFolder(documentInDB.getDocumentFiles(), path);
        } catch (IOException e) {
            return DocumentDTO.builder()
                    .errorMessage("Error archiving files")
                    .build();
        }

        List<DocumentFile> newFiles;

        try {
            newFiles = saveNewFilesToPath(path, files);
        } catch (IOException e) {
            return DocumentDTO.builder()
                    .errorMessage("Error saving files")
                    .build();
        }

        Document document = Document.builder()
                .fileName(documentDto.getFileName())
                .fileNumber(documentDto.getFileNumber())
                .revisionNo(documentDto.getRevisionNo())
                .revisionDate(documentDto.getRevisionDate())
                .path(path)
                .department(department)
                .documentFiles(newFiles)
                .currentStatus(status)
                .fileDBNumber(UUID.randomUUID().toString())
                .projectName(documentDto.getProjectName())
                .contractName(documentDto.getContractName())
                .reasonForUpdate(documentDto.getReasonForUpdate())
                .createdBy(userId)
                .createdByUser(user.getUserName())
                .build();

        Document savedDocument = documentRepository.save(document);

        for (DocumentFile f : newFiles) {
            f.setDocument(savedDocument);
            documentFileRepository.save(f);
        }

        // revision record
        DocumentRevision revision = DocumentRevision.builder()
                .fileName(documentInDB.getFileName())
                .fileNumber(documentInDB.getFileNumber())
                .revisionNo(documentInDB.getRevisionNo())
                .revisionDate(documentInDB.getRevisionDate())
                .path(documentInDB.getPath())
                .department(department)
                .currentStatus(status)
                .documentFiles(archivedFiles)
                .fileDBNumber(documentInDB.getFileDBNumber())
                .createdBy(documentInDB.getCreatedBy())
                .createdByUser(documentInDB.getCreatedByUser())
                .projectName(documentInDB.getProjectName())
                .contractName(documentInDB.getContractName())
                .reasonForUpdate(documentInDB.getReasonForUpdate())
                .build();

        documentRevisionRepository.save(revision);

        documentRepository.delete(documentInDB);

        return mapTODto(savedDocument, department, status);
    }

    // 🆕 NEW DOCUMENT
    return saveNewDocument(documentDto, files, path, department, status, userId);
}

    private boolean isSmaller(String uiRevisionNo, String dbRevisionNo) {
		String numberPart = uiRevisionNo.substring(1); // "01"

		// Convert to integer
		int uiRevisionNoValue = Integer.parseInt(numberPart);

		numberPart = dbRevisionNo.substring(1); // "01"

		// Convert to integer
		int dbRevisionNoValue = Integer.parseInt(numberPart);

		if (dbRevisionNoValue >= uiRevisionNoValue) {
			return true;
		}

		return false;
	}

    private List<DocumentFile> moveFilesToArchiveFolder(List<DocumentFile> files, String path)
            throws IOException {

        List<DocumentFile> savedFiles = new ArrayList<>();

        String cleanPath = normalizePath(path);

        String archiveFolderPath = basePath + "\\" + cleanPath + "\\archive\\";

        File directory = new File(archiveFolderPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (DocumentFile file : files) {

            String original = file.getFileName();

            String name = original.substring(0, original.lastIndexOf("."));
            String ext = original.substring(original.lastIndexOf(".") + 1);

            String newFileName = name + "_" + System.currentTimeMillis() + "." + ext;

            Path sourcePath = Paths.get(file.getFilePath());
            Path targetPath = Paths.get(archiveFolderPath + newFileName);

            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            DocumentFile newFile = new DocumentFile();
            newFile.setFileName(newFileName);
            newFile.setFilePath(targetPath.toString());
            newFile.setFileType(ext);

            savedFiles.add(newFile);
        }

        return savedFiles;
    }

    private DocumentDTO mapTODto(Document document, Department department, Status status) {

        return DocumentDTO.builder()
                .fileName(document.getFileName())
                .fileNumber(document.getFileNumber())
                .revisionNo(document.getRevisionNo())
                .revisionDate(document.getRevisionDate())
                .path(document.getPath())
                .department(department.getName())
                .currentStatus(status.getName())
                .projectName(document.getProjectName())
                .contractName(document.getContractName())
                .reasonForUpdate(document.getReasonForUpdate())
                .build();
    }

	private DocumentDTO saveNewDocument(
            DocumentDTO documentDto,
            List<MultipartFile> files,
            String path,
            Department department,
            Status status,
            String userId) {

        List<DocumentFile> newDocumentFiles;
        User user = userRepository.findById(userId).get();

        try {
            newDocumentFiles = saveNewFilesToPath(path, files); 
        } catch (IOException e) {
            return DocumentDTO.builder()
                    .errorMessage("Error saving files to filesystem")
                    .build();
        }

        Document document = Document.builder()
                .fileName(documentDto.getFileName())
                .fileNumber(documentDto.getFileNumber())
                .revisionNo("R01")
                .revisionDate(documentDto.getRevisionDate())
                .path(path)  
                .department(department)
                .currentStatus(status)
                .fileDBNumber(UUID.randomUUID().toString())
                .projectName(documentDto.getProjectName())
                .contractName(documentDto.getContractName())
                .reasonForUpdate(documentDto.getReasonForUpdate())
                .createdBy(userId)
                .createdByUser(user.getUserName())
                .build();

        Document savedDocument = documentRepository.save(document);

        for (DocumentFile file : newDocumentFiles) {
            file.setDocument(savedDocument);
            documentFileRepository.save(file);
        }

        return mapTODto(savedDocument, department, status);
    }

    
    @Override
    @Transactional
    public void saveMultipleDocuments(DocumentDTO dto, List<MultipartFile> files) throws Exception {

        for (MultipartFile file : files) {
            saveDocument(dto, file);
        }
    }

    private List<DocumentFile> saveNewFilesToSubFolder(SubFolder subFolder, List<MultipartFile> files)
			throws IOException {
		List<DocumentFile> savedFiles = new ArrayList<>();

		// Create full path for subfolder
		String subFolderPath = basePath + "\\" + subFolder.getFolder().getName() + "\\" + subFolder.getName() + "\\";

		// Ensure the directory exists
		File directory = new File(subFolderPath);
		if (!directory.exists()) {
			directory.mkdirs(); // create folder if not exists
		}

		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				try {

					String targetFileName = file.getOriginalFilename().split("\\.")[0] + System.currentTimeMillis()
							+ "." + file.getOriginalFilename().split("\\.")[1];
					// Destination file path
					String filePath = subFolderPath + targetFileName;

					// Save the file to disk
					File dest = new File(filePath);
					file.transferTo(dest);

					// Create and add DocumentFile record (you may adjust fields)
					DocumentFile documentFile = new DocumentFile();
					documentFile.setFileName(targetFileName);
					documentFile.setFilePath(filePath);
					documentFile.setFileType(getExtension(dest));
					// assuming relation exists
					DocumentFile savedDocumentFile = documentFileRepository.save(documentFile);

					savedFiles.add(savedDocumentFile);

				} catch (IOException e) {
					throw e;
					// Optional: log or throw a custom exception
				}
			}
		}

		return savedFiles;
	}

    private List<DocumentFile> saveNewFilesToPath(String path, List<MultipartFile> files)
            throws IOException {

        List<DocumentFile> savedFiles = new ArrayList<>();
        
        path = path.trim()
                .replace(" / ", File.separator)
                .replace("/", File.separator)
                .replace("\\\\", "\\");

        String fullPath = basePath + File.separator + path + File.separator;

        File directory = new File(fullPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (MultipartFile file : files) {

            if (!file.isEmpty()) {

                String original = file.getOriginalFilename();

                String name = original.substring(0, original.lastIndexOf("."));
                String ext = original.substring(original.lastIndexOf(".") + 1);

                String newFileName = name + "_" + System.currentTimeMillis() + "." + ext;

                Path filePath = Paths.get(fullPath + newFileName);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                DocumentFile docFile = new DocumentFile();
                docFile.setFileName(newFileName);
                docFile.setFilePath(filePath.toString());
                docFile.setFileType(ext);

                savedFiles.add(docFile);
            }
        }

        return savedFiles;
    }

    private String normalizePath(String path) {

        if (path == null) return "";

        return path
                .trim()
                .replaceAll("\\s*/\\s*", "\\\\")  
                .replaceAll("\\\\+", "\\\\")    
                .replaceAll("\\s+$", "");       
    }

	public static String getExtension(File file) {
		String name = file.getName();
		int lastIndex = name.lastIndexOf(".");
		if (lastIndex != -1 && lastIndex < name.length() - 1) {
			return name.substring(lastIndex + 1);
		}
		return "";
	}

    @Override
    @Transactional
    public void updateDocument(Long id, DocumentDTO dto, MultipartFile file) throws Exception {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (dto.getFileName() != null) {
			document.setFileName(dto.getFileName());
		}

		if (dto.getFileNumber() != null) {
			document.setFileNumber(dto.getFileNumber());
		}
		if (dto.getRevisionNo() != null){
        document.setRevisionNo(dto.getRevisionNo());
		}
		if (dto.getRevisionDate() != null) {
        document.setRevisionDate(dto.getRevisionDate());
		}

        document.setProjectName(dto.getProjectName());
        document.setContractName(dto.getContractName());

        if (dto.getDepartment() != null) {
            Department department = departmentRepository
                    .findById(Long.parseLong(dto.getDepartment()))
                    .orElse(null);

            document.setDepartment(department);
        }

        if (dto.getCurrentStatus() != null) {
            Status status = statusRepository
                    .findById(Long.parseLong(dto.getCurrentStatus()))
                    .orElse(null);

            document.setCurrentStatus(status);
        }

        document.setUpdatedAt(LocalDateTime.now());

        if (dto.getFolderId() != null) {

            Folder folder = folderRepository.findById(dto.getFolderId())
                    .orElseThrow(() -> new RuntimeException("Folder not found"));

            document.setFolder(folder);
            document.setPath(buildFolderPath(folder));
        }

        document = documentRepository.save(document);

        if (file != null && !file.isEmpty()) {

            // OPTIONAL: delete old files (recommended)
            if (document.getDocumentFiles() != null) {
                for (DocumentFile old : document.getDocumentFiles()) {
                    try {
                        Files.deleteIfExists(Paths.get(old.getFilePath()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                document.getDocumentFiles().clear();
            }

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String storedFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path filePath = uploadPath.resolve(storedFileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            DocumentFile docFile = new DocumentFile();
            docFile.setFileName(storedFileName);
            docFile.setFileType(file.getContentType());
            docFile.setFilePath(filePath.toString());
            docFile.setDocument(document);

            documentFileRepository.save(docFile);
        }

        DocumentRevision revision = new DocumentRevision();

        revision.setFileName(document.getFileName());
		revision.setFileNumber(document.getFileNumber());
        revision.setRevisionNo(dto.getRevisionNo());
        revision.setRevisionDate(dto.getRevisionDate());
        revision.setDocument(document);

        documentRevisionRepository.save(revision);
    }

    // @Override
    // public List<DocumentRevisionDTO> getDocumentVersions(String fileNumber) {

    //     return documentRevisionRepository.findAllByFileNumberAndFileName(fileNumber, null)
    //             .stream()
    //             .peek(rev -> {
    //                 if (rev.getDocumentFiles() != null && !rev.getDocumentFiles().isEmpty()) {
    //                     rev.setPath(rev.getDocumentFiles().get(0).getFilePath()); // ADD THIS FIELD
    //                 }
    //             })
    //             .collect(Collectors.toList());
    // }

    @Override
    public List<DocumentRevisionDTO> getDocumentVersions(String fileNumber) {

        return documentRevisionRepository
                .findAllByFileNumberAndFileName(fileNumber, null);
    }

    @Override
    public void sendDocument(SendDocumentDTO dto) {
        
    }


    @Override
	public void markNotRequired(NotRequiredDTO dto, String userId) {

		Document document = documentRepository.findById(dto.getDocumentId())
				.orElseThrow(() -> new RuntimeException("Document not found"));

		List<DocumentFile> archivedFiles = new ArrayList<>();

		try {
			archivedFiles = this.moveFilesToArchiveFolder(document.getDocumentFiles(), dto.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

		document.getDocumentFiles().clear();

		for (DocumentFile archivedFile : archivedFiles) {
			archivedFile.setDocument(document);
			documentFileRepository.save(archivedFile);
			document.getDocumentFiles().add(archivedFile);
		}

		document.setNotRequired(true);
		document.setNotRequiredBy(userId);

		documentRepository.save(document);
	}

    @Override
    public String saveOrSendDocument(SendDocumentDTO dto, String userId, String baseUrl) {
        if ("Send".equals(dto.getStatus()) && (dto.getSendToUserId() == null || dto.getSendToUserId().isBlank())) {
            throw new RuntimeException("Recipient is required to send document");
        }

		System.out.println("DTO SendToUserId= " + dto.getSendToUserId());
		System.out.println("DTO SendTo= " + dto.getSendTo());

        SendDocument sendDocument = mapDTOToSendDocument(dto, userId);

        sendDocument = sendDocumentRepository.save(sendDocument);

        if (dto.getStatus().equals("Send")) {// send email
			String host = emailHost; // e.g., smtp.gmail.com
			final String username = emailUserName;
			final String password = emailPassword; // or actual password (less secure)

			// Email Properties
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", "587");

			// Create session
			Session session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			try {
				// Construct email
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(username));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(dto.getSendTo()));

				// ✅ Set CC recipients
				// message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(dto.getSendCc()));
                if(dto.getSendCc() != null && !dto.getSendCc().isBlank()) {
                    message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(dto.getSendCc()));
                }
				message.setSubject(dto.getSendSubject());
				User userTo = userRepository.findByEmailId(sendDocument.getSendTo()).get();
				User sendBy = userRepository.findById(sendDocument.getCreatedBy()).get();
				// HTML content (from your screenshot)
				String htmlContent = String.format(
						"""
								 <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #ddd;">
								    <div style="background-color: #004B87; color: white; padding: 15px; font-size: 20px; font-weight: bold;">
								        New Document Notification
								    </div>
								    <div style="padding: 20px; color: #333;">
								        <p>Dear %s,</p>
								        <p><strong>%s</strong> has sent a document.</p>

								        <table style="width: 100%%; margin-top: 15px; font-size: 14px;">
								            <tr><td><strong>Reason</strong></td><td>: %s</td></tr>
								            <tr><td><strong>Response Expected</strong></td><td>: %s</td></tr>
								            <tr><td><strong>Target Date</strong></td><td>: %s</td></tr>
								            <tr><td><strong>Attachments</strong></td><td>: %s</td></tr>
								        </table>

								        <p style="margin-top: 30px;">Regards,<br/>%s</p>
								    </div>
								    <div style="background-color: green; padding: 15px; text-align: center;">
								        <a href="%s" style="background-color: green; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px; margin-right: 10px;">Document Location</a>
								    </div>
								</div>
								""",
						userTo.getUserName(), // %s → Dear <name>
						sendBy.getUserName(), sendDocument.getSendReason(), sendDocument.getResponseExpected(),
						sendDocument.getTargetResponseDate(), sendDocument.getAttachmentName(), sendBy.getUserName(),
						baseUrl + "/document.html"

				// %s → <sender> has sent a document
				);
				message.setContent(htmlContent, "text/html");

				// Send message
				Transport.send(message);
				System.out.println("Email sent successfully!");

			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	private SendDocument mapDTOToSendDocument(SendDocumentDTO dto, String userId) {

        SendDocument sendDocument;

        if (dto.getId() != null) {
            sendDocument = sendDocumentRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Draft not found"));
        } else {
            sendDocument = new SendDocument();
        }

        if (dto.getDocId() == null) {
            throw new RuntimeException("Document ID is missing.");
        }

        Document document = documentRepository.findById(dto.getDocId())
                .orElseThrow(() -> new RuntimeException("Document not found"));

        sendDocument.setDocument(document);
        sendDocument.setAttachmentName(dto.getAttachmentName());
        sendDocument.setSendTo(dto.getSendTo());
        sendDocument.setSendToUserId(dto.getSendToUserId());
        sendDocument.setSendCc(dto.getSendCc());
        sendDocument.setSendCcUserId(dto.getSendCcUserId());
        sendDocument.setSendSubject(dto.getSendSubject());
        sendDocument.setSendReason(dto.getSendReason());
        sendDocument.setResponseExpected(dto.getResponseExpected());
        sendDocument.setTargetResponseDate(dto.getTargetResponseDate());
        sendDocument.setStatus(dto.getStatus());
        sendDocument.setCreatedBy(userId);

        return sendDocument;
    }



    private Map<String, MetaDataDto> validate(String key, Map<String, Integer> headerIndexMap, List<String> row,
			String... args)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
		Map<String, MetaDataDto> map = new HashMap<>();
		int index = headerIndexMap.get(key);
		Method method = DocumentServiceImpl.class.getMethod(Constant.METADATA_UPLOAD_VALIDATION_MAP.get(key),
				String[].class); // get method
		String errorMessageFileName = (String) method.invoke(documentService, (Object) args);
		MetaDataDto metadataDtoFileName = MetaDataDto.builder().errorMessage(errorMessageFileName).value(row.get(index))
				.build();
		map.put(key, metadataDtoFileName);
		return map;
	}

    @Override
    public List<Map<String, MetaDataDto>> validateMetadata(List<List<String>> rows, String userId, String userRoleName)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
        List<Map<String, MetaDataDto>> validationResults = new ArrayList<>();
        Map<String, Integer> headerIndexMap = new HashMap<>();
        List<String> headers = rows.get(0);
        for (int i = 0; i < headers.size(); i++) {
            headerIndexMap.put(headers.get(i), i);
        }
        for (int i = 1; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            Map<String, MetaDataDto> rowValidationResult = new HashMap<>();
            for (String key : Constant.METADATA_UPLOAD_VALIDATION_MAP.keySet()) {
                rowValidationResult.putAll(validate(key, headerIndexMap, row, row.get(headerIndexMap.get(key))));
            }
            validationResults.add(rowValidationResult);
        }
        return validationResults;

    }

    @Override
    public void processBulkZip(Long id, MultipartFile zipFile,
            Map<Long, List<DocumentDTO>> bulkStore) throws Exception {

        List<DocumentDTO> docs = bulkStore.get(id);

        if (docs == null) {
            throw new RuntimeException("Metadata not found");
        }

        Path tempDir = Files.createTempDirectory("bulk");

        // unzip
        java.util.zip.ZipInputStream zis =
                new java.util.zip.ZipInputStream(zipFile.getInputStream());

        Map<String, Path> fileMap = new HashMap<>();

        java.util.zip.ZipEntry entry;

        while ((entry = zis.getNextEntry()) != null) {

            if (!entry.isDirectory()) {

                Path filePath = tempDir.resolve(entry.getName());

                Files.createDirectories(filePath.getParent());
                Files.copy(zis, filePath, StandardCopyOption.REPLACE_EXISTING);

                fileMap.put(entry.getName(), filePath);
            }
        }

        zis.close();

        // create documents
        for (DocumentDTO dto : docs) {

            Path filePath = fileMap.get(dto.getFileName());

            if (filePath == null) {
                throw new RuntimeException(dto.getFileName() + " missing in zip");
            }

            MultipartFile file =
                    new org.springframework.mock.web.MockMultipartFile(
                            dto.getFileName(),
                            dto.getFileName(),
                            Files.probeContentType(filePath),
                            Files.newInputStream(filePath)
                    );

            saveDocument(dto, file);
        }
    }
}