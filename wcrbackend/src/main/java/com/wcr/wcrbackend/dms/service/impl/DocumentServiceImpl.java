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
import com.wcr.wcrbackend.dms.dto.MetaDataDto;
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

        List<Document> docs = documentRepository.findAll();

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

            dto.setPath(buildFolderPath(d.getFolder()));

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

        document.setCreatedBy("SYSTEM");
        document.setCreatedAt(LocalDateTime.now());

        // document = documentRepository.save(document);

        if (dto.getFolderId() != null) {

            Folder folder = folderRepository.findById(dto.getFolderId())
                    .orElseThrow(() -> new RuntimeException("Folder not found"));

            document.setFolder(folder);
        }

        if (dto.getFolderId() != null) {

            Folder folder = folderRepository.findById(dto.getFolderId())
                .orElseThrow(() -> new RuntimeException("Folder not found"));

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

        DocumentFile file = documentFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        Path path = Paths.get(file.getFilePath());

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\"")
                .body(resource);
    }

    private String buildFolderPath(Folder folder) {

        if (folder == null) return "";

        StringBuilder path = new StringBuilder();

        Folder current = folder;

        while (current != null) {

            path.insert(0, current.getName() + " / ");

            if (current.getParentId() != null) {
                current = folderRepository.findById(current.getParentId()).orElse(null);
            } else {
                current = null;
            }
        }

        return path.toString();
    }

    @Override
	@Transactional
	public DocumentDTO uploadFileWithMetaData(DocumentDTO documentDto, List<MultipartFile> files, String userId) {
		Folder folder = folderRepository.findByName(documentDto.getFolder()).get();
		SubFolder subFolder = subFolderRepository.findByName(documentDto.getSubFolder()).get();
		Department department = departmentRepository.findByName(documentDto.getDepartment()).get();
		Status status = statusRepository.findByName(documentDto.getCurrentStatus()).get();
		User user = userRepository.findById(userId).get();
		
		Optional<Document> documentInDBOptional = documentRepository.findByFileName(documentDto.getFileName());
		// 1. If file name is same but file number is different
		if (documentInDBOptional.isPresent()) {
			Document document = documentInDBOptional.get();
			if (!document.getFileNumber().equals(documentDto.getFileNumber())) {
				return DocumentDTO.builder().fileName(document.getFileName()).fileNumber(document.getFileNumber())
						.revisionNo(document.getRevisionNo()).revisionDate(document.getRevisionDate())
						.folder(folder.getName()).subFolder(subFolder.getName()).department(department.getName())
						.currentStatus(status.getName())
						.errorMessage("File name already exists with File number: " + document.getFileNumber()
								+ ". Change the File name or File number to accept.")
						.projectName(document.getProjectName()).contractName(document.getContractName()).build();
			}
		}

		documentInDBOptional = documentRepository.findByFileNumber(documentDto.getFileNumber());
		// 2. If file number is same but file name is different
		if (documentInDBOptional.isPresent()) {
			Document document = documentInDBOptional.get();
			if (!document.getFileName().equals(documentDto.getFileName())) {
				return DocumentDTO.builder().fileName(document.getFileName()).fileNumber(document.getFileNumber())
						.revisionNo(document.getRevisionNo()).revisionDate(document.getRevisionDate())
						.folder(folder.getName()).subFolder(subFolder.getName()).department(department.getName())
						.currentStatus(status.getName())
						.errorMessage("File number already exists for File name: " + document.getFileName()
								+ ". Change the File name or File number to accept.")
						.projectName(document.getProjectName()).contractName(document.getContractName()).build();
			}
		}

		// If file exists with same FileName & FileNumber UI should be reported
		documentInDBOptional = documentRepository.findByFileNameAndFileNumber(documentDto.getFileName(),
				documentDto.getFileNumber());

		// 3. If Revisionnumber is smaller than report to user
		if (documentInDBOptional.isPresent()) {
			Document document = documentInDBOptional.get();
			String uiRevisionNo = documentDto.getRevisionNo();
			String dbRevisionNo = document.getRevisionNo();
			if (isSmaller(uiRevisionNo, dbRevisionNo)) {
				return DocumentDTO.builder().fileName(document.getFileName()).fileNumber(document.getFileNumber())
						.revisionNo(document.getRevisionNo()).revisionDate(document.getRevisionDate())
						.folder(folder.getName()).subFolder(subFolder.getName()).department(department.getName())
						.currentStatus(status.getName())
						.errorMessage("File Name: " + documentDto.getFileName() + ", File Number: "
								+ documentDto.getFileNumber()
								+ " already has file with same Revision number. The Revision number has to be more than "
								+ dbRevisionNo)
						.projectName(document.getProjectName()).contractName(document.getContractName()).build();

			}
		}

		//
		if (documentInDBOptional.isPresent()) {
			// update the revision and file number and archive file
			Document documentInDB = documentInDBOptional.get();

			List<DocumentFile> archivedDocumentFiles;
			try {
				archivedDocumentFiles = moveFilesToArchiveFolder(documentInDB.getDocumentFiles(), subFolder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return DocumentDTO.builder().errorMessage("Error archiving files to the filesystem").build();
			}

			List<DocumentFile> newDocumentFiles;
			try {
				newDocumentFiles = saveNewFilesToSubFolder(subFolder, files);
			} catch (IOException e) {
				return DocumentDTO.builder().errorMessage("Error saving files to the filesystem").build();
			}
			List<SendDocument> sendDocuments = documentInDB.getSendDocument();
			Document document = Document.builder().fileName(documentDto.getFileName())
					.fileNumber(documentDto.getFileNumber()).revisionNo(documentDto.getRevisionNo())
					.revisionDate(documentDto.getRevisionDate()).folder(folder).subFolder(subFolder)
					.department(department).fileDBNumber(UUID.randomUUID().toString()).documentFiles(newDocumentFiles)
					.currentStatus(status).projectName(documentDto.getProjectName())
					.contractName(documentDto.getContractName()).reasonForUpdate(documentDto.getReasonForUpdate())
					.createdBy(userId)
					.createdByUser(user.getUserName()).build();
			List<SendDocument> newSendDocuments = new ArrayList<>();
			for(SendDocument sendDocument : sendDocuments) {
				SendDocument newSendDocument  = SendDocument.builder()
						.attachmentName(sendDocument.getAttachmentName())
						.createdBy(sendDocument.getCreatedBy())
						.createdAt(sendDocument.getCreatedAt())
						.document(document)
						.responseExpected(sendDocument.getResponseExpected())
						.sendReason(sendDocument.getSendReason())
						.sendSubject(sendDocument.getSendSubject())
						.sendTo(sendDocument.getSendTo())
						.sendToUserId(sendDocument.getSendToUserId())
						.status(sendDocument.getStatus())
						.targetResponseDate(sendDocument.getTargetResponseDate())
						.updatedAt(sendDocument.getUpdatedAt())
						.build();
				newSendDocuments.add(newSendDocument);
			}
			document.setSendDocument(newSendDocuments);
			// Save the new Document first
			Document savedNewDocument = documentRepository.save(document);
			documentRepository.flush();

			// Save DocumentFiles and link to savedDocument
			// List<DocumentFile> savedNewDocumentFiles = new ArrayList<>();
			for (DocumentFile documentFile : newDocumentFiles) {
				// documentFile.setDocument(savedDocument);
				documentFile.setDocument(savedNewDocument);
				documentFileRepository.save(documentFile);
				documentFileRepository.flush();
				// savedArchivedDocumentFiles.add(documentFile);
			}
			Optional<Document> latestFromDB = documentRepository.findById(documentInDB.getId());

			// Create DocumentRevision linked to old document (still persistent here)
			DocumentRevision documentRevision = DocumentRevision.builder().fileName(documentInDB.getFileName())
					.fileNumber(documentInDB.getFileNumber()).revisionNo(documentInDB.getRevisionNo())
					.revisionDate(documentInDB.getRevisionDate()).folder(folder).subFolder(subFolder)
					.department(department).currentStatus(status).documentFiles(archivedDocumentFiles)
					.fileDBNumber(documentInDB.getFileDBNumber()).createdBy(documentInDB.getCreatedBy())
					// .document(latestFromDB.get()) // still valid at this point
					.projectName(documentInDB.getProjectName()).contractName(documentInDB.getContractName())
					.reasonForUpdate(documentInDB.getReasonForUpdate())
					.createdByUser(documentInDB.getCreatedByUser()).build();

			documentRevisionRepository.save(documentRevision);
			documentRevisionRepository.flush();
			// ✅ Now it's safe to delete the old document
			List<DocumentFile> savedArchivedDocumentFiles = new ArrayList<>();
			for (DocumentFile documentFile : archivedDocumentFiles) {
				// documentFile.setDocument(savedDocument);
				documentFile.setDocumentRevision(documentRevision);
				documentFileRepository.save(documentFile);
				documentFileRepository.flush();
				savedArchivedDocumentFiles.add(documentFile);
			}

			documentRepository.delete(latestFromDB.get());

			return mapTODto(savedNewDocument, folder, subFolder, department, status);

		}

		return saveNewDocument(documentDto, files, folder, subFolder, department, status, userId);
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

    private List<DocumentFile> moveFilesToArchiveFolder(List<DocumentFile> files, SubFolder subFolder)
			throws IOException {
		List<DocumentFile> savedFiles = new ArrayList<>();

		// Create full path for subfolder
		String archiveFolderPath = basePath + "\\" + subFolder.getFolder().getName() + "\\" + subFolder.getName()
				+ "\\archive\\";

		String subFolderPath = basePath + "\\" + subFolder.getFolder().getName() + "\\" + subFolder.getName() + "\\";
		// Ensure the directory exists
		File directory = new File(archiveFolderPath);
		if (!directory.exists()) {
			directory.mkdirs(); // create folder if not exists
		}

		for (DocumentFile file : files) {
			try {
				String targetFileName = file.getFileName().split("\\.")[0] + System.currentTimeMillis() + "."
						+ file.getFileName().split("\\.")[1];
				Path sourcePath = Paths.get(file.getFilePath());
				Path targetPath = Paths.get(archiveFolderPath + targetFileName);
				DocumentFile documentFile = new DocumentFile();
				documentFile.setFilePath(archiveFolderPath + targetFileName);
				documentFile.setFileName(targetFileName);
				documentFile.setFileType(targetFileName.split("\\.")[1]);
				savedFiles.add(documentFile);
				Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

			} catch (IOException e) {
				throw e;
				// Optional: log or throw a custom exception
			}
		}

		return savedFiles;
	}

    private DocumentDTO mapTODto(Document document, Folder folder, SubFolder subFolder, Department department,
			Status status) {
		return DocumentDTO.builder().fileName(document.getFileName()).fileNumber(document.getFileNumber())
				.revisionNo(document.getRevisionNo()).revisionDate(document.getRevisionDate()).folder(folder.getName())
				.subFolder(subFolder.getName()).department(department.getName()).currentStatus(status.getName())
				.projectName(document.getProjectName()).contractName(document.getContractName())
				.reasonForUpdate(document.getReasonForUpdate()).build();
	}

	private DocumentDTO saveNewDocument(DocumentDTO documentDto, List<MultipartFile> files, Folder folder,
			SubFolder subFolder, Department department, Status status, String userId) {
		List<DocumentFile> newDocumentFiles;
		User user = userRepository.findById(userId).get();
		try {
			newDocumentFiles = saveNewFilesToSubFolder(subFolder, files);
		} catch (IOException e) {
			return DocumentDTO.builder().errorMessage("Error saving files to the filesystem").build();
		}

		Document document = Document.builder().fileName(documentDto.getFileName())
				.fileNumber(documentDto.getFileNumber()).revisionNo("R01").revisionDate(documentDto.getRevisionDate())
				.folder(folder).subFolder(subFolder).department(department).currentStatus(status)
				.fileDBNumber(UUID.randomUUID().toString()).projectName(documentDto.getProjectName())
				.contractName(documentDto.getContractName()).reasonForUpdate(documentDto.getReasonForUpdate())
				.createdBy(userId).createdByUser(user.getUserName()).build();
		Document savedDocument = documentRepository.save(document);
		for (DocumentFile documentFile : newDocumentFiles) {
			documentFile.setDocument(savedDocument);
			documentFileRepository.save(documentFile);
		}
		return mapTODto(savedDocument, folder, subFolder, department, status);
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

	public static String getExtension(File file) {
		String name = file.getName();
		int lastIndex = name.lastIndexOf(".");
		if (lastIndex != -1 && lastIndex < name.length() - 1) {
			return name.substring(lastIndex + 1);
		}
		return "";
	}

    @Override
    public void updateDocument(Long id, DocumentDTO dto, MultipartFile file) throws Exception {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        document.setFileName(dto.getFileName());
        document.setFileNumber(dto.getFileNumber());
        document.setRevisionNo(dto.getRevisionNo());
        document.setRevisionDate(dto.getRevisionDate());

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
        }

        document = documentRepository.save(document);

        if (file != null && !file.isEmpty()) {

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String storedFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

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
    public List<DocumentRevision> getDocumentVersions(String fileNumber) {
        return documentRevisionRepository.findAllByFileNumberAndFileName(fileNumber, null)
                .stream()
                .map(dto -> {
                    DocumentRevision rev = new DocumentRevision();
                    rev.setFileName(dto.getFileName());
                    rev.setFileNumber(dto.getFileNumber());
                    rev.setRevisionNo(dto.getRevisionNo());
                    rev.setDocument(null); // Not setting the document reference here
                    return rev;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void sendDocument(SendDocumentDTO dto) {
        
    }

    @Override
    public void markNotRequired(Long id) {
            Document document = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Document not found"));
    
            Status notRequiredStatus = statusRepository.findByName("Not Required")
                    .orElseThrow(() -> new RuntimeException("Status 'Not Required' not found"));
    
            document.setCurrentStatus(notRequiredStatus);
            document.setUpdatedAt(LocalDateTime.now());
    
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
		Document document = documentRepository.getById(dto.getDocId());
		return SendDocument.builder().id(dto.getId()).attachmentName(dto.getAttachmentName()).document(document)
				.sendTo(dto.getSendTo()).sendToUserId(dto.getSendToUserId()).sendCc(dto.getSendCc())
				.sendCcUserId(dto.getSendCcUserId()).sendSubject(dto.getSendSubject()).sendReason(dto.getSendReason())
				.responseExpected(dto.getResponseExpected()).targetResponseDate(dto.getTargetResponseDate())
				.status(dto.getStatus()).createdBy(userId).build();
	}

    @Override
	public List<Map<String, MetaDataDto>> validateMetadata(List<List<String>> rows, String userId, String userRoleName)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
		List<Map<String, MetaDataDto>> mapList = new ArrayList<>();

		List<String> firstRow = rows.get(0);
		Map<String, Integer> headerIndexMap = new HashMap<>();
		for (int i = 0; i < firstRow.size(); i++) {
			headerIndexMap.put(firstRow.get(i), i);
		}
		List<List<String>> rowsWithoutFirst = rows.size() > 1 ? new ArrayList<>(rows.subList(1, rows.size()))
				: new ArrayList<>();

		for (List<String> row : rowsWithoutFirst) {
			Map<String, MetaDataDto> map = new HashMap<>();
			mapList.add(
					validate(Constant.FILE_NAME, headerIndexMap, row, row.get(headerIndexMap.get(Constant.FILE_NAME)),
							row.get(headerIndexMap.get(Constant.FILE_NUMBER))));
			mapList.add(validate(Constant.FILE_NUMBER, headerIndexMap, row,
					row.get(headerIndexMap.get(Constant.FILE_NUMBER)),
					row.get(headerIndexMap.get(Constant.FILE_NAME))));
			mapList.add(validate(Constant.REVISION_NUMBER, headerIndexMap, row,
					row.get(headerIndexMap.get(Constant.FILE_NAME)), row.get(headerIndexMap.get(Constant.FILE_NUMBER)),
					row.get(headerIndexMap.get(Constant.REVISION_NUMBER))));
			mapList.add(validate(Constant.REVISION_DATE, headerIndexMap, row,
					row.get(headerIndexMap.get(Constant.REVISION_DATE))));

			mapList.add(validate(Constant.PROJECT_NAME, headerIndexMap, row,
					row.get(headerIndexMap.get(Constant.PROJECT_NAME)), userId, userRoleName));
			mapList.add(validate(Constant.CONTRACT_NAME, headerIndexMap, row,
					row.get(headerIndexMap.get(Constant.CONTRACT_NAME)), userId, userRoleName));

			mapList.add(validate(Constant.FOLDER, headerIndexMap, row, row.get(headerIndexMap.get(Constant.FOLDER))));
			mapList.add(validate(Constant.SUB_FOLDER, headerIndexMap, row, row.get(headerIndexMap.get(Constant.FOLDER)),
					row.get(headerIndexMap.get(Constant.SUB_FOLDER))));
			mapList.add(validate(Constant.DEPARTMENT, headerIndexMap, row,
					row.get(headerIndexMap.get(Constant.DEPARTMENT))));
			mapList.add(validate(Constant.STATUS, headerIndexMap, row, row.get(headerIndexMap.get(Constant.STATUS))));
			mapList.add(validate(Constant.UPLOAD_DOCUMENT, headerIndexMap, row,
					row.get(headerIndexMap.get(Constant.UPLOAD_DOCUMENT))));

			mapList.add(map);
		}

		return mapList;
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
}