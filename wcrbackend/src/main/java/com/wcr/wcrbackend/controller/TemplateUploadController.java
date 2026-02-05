package com.wcr.wcrbackend.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.io.File;
import java.nio.file.Files;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wcr.wcrbackend.common.CommonConstants;
import com.wcr.wcrbackend.common.FileUploads;
import com.wcr.wcrbackend.common.OSValidator;
import com.wcr.wcrbackend.reference.model.TrainingType;
import com.wcr.wcrbackend.service.ITemplateUploadService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/templates")
public class TemplateUploadController {

	private static final Logger logger = Logger.getLogger(TemplateUploadController.class);

	@Autowired
	private ITemplateUploadService service;

	@GetMapping
	public ResponseEntity<List<TrainingType>> getTemplatesList() {
		try {
			List<TrainingType> list = service.getTemplatesList();
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			logger.error("getTemplatesList error", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> uploadTemplate(@ModelAttribute TrainingType obj, HttpSession session) {

		Map<String, Object> response = new HashMap<>();

		try {
			String userId = (String) session.getAttribute("userId");
			String userName = (String) session.getAttribute("userName");

			if (userId == null || userName == null) {
			    response.put("status", "error");
			    response.put("message", "Session expired. Please login again.");
			    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
			}
			
			obj.setUploaded_by(userId);
			obj.setUser_name(userName);

			String uploadedOn = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
			obj.setUploaded_on(uploadedOn);

			MultipartFile file = obj.getTemplateFile();
			if (file == null || file.isEmpty()) {
				response.put("status", "error");
				response.put("message", "No file uploaded");
				return ResponseEntity.badRequest().body(response);
			}

			String originalFileName = file.getOriginalFilename();
			String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

			String templateName = obj.getTemplate_name();

			// declare once
			String archivedFileName = null;

			// build active filename
			String activeFileName = templateName + fileExtension;

			// ALWAYS check DB intent, not filesystem state
			boolean isReUpload = Files.exists(Paths.get(CommonConstants.TEMPLATE_FILEPATH, activeFileName));

			if (isReUpload) {

				String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

				archivedFileName = templateName + "_" + timeStamp + fileExtension;

				Files.move(Paths.get(CommonConstants.TEMPLATE_FILEPATH, activeFileName),
						Paths.get(CommonConstants.TEMPLATE_OLD_FILEPATH, archivedFileName),
						StandardCopyOption.REPLACE_EXISTING);

				// 🔴 FORCE archived filename into attachment
				obj.setAttachment(archivedFileName);
			} else {
				// first upload
				obj.setAttachment(activeFileName);
			}

			// save new active file
			FileUploads.singleFileSaving(file, CommonConstants.TEMPLATE_FILEPATH, activeFileName);

			obj.setCommonAttachment(activeFileName);
			obj.setStatus(CommonConstants.ACTIVE);

			// DB
			boolean flag = service.uploadTemplate(obj);
			response.put("status", flag ? "success" : "error");
			response.put("message", flag ? "Template uploaded successfully" : "Template upload failed");

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("uploadTemplate error", e);
			response.put("status", "error");
			response.put("message", "Server error while uploading template");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> deleteTemplate(@PathVariable String id) {

		Map<String, Object> response = new HashMap<>();

		try {
			TrainingType obj = new TrainingType();
			obj.setId(id);

			boolean flag = service.deleteTemplate(obj);

			if (flag) {
				response.put("status", "success");
				response.put("message", "Template deleted successfully");
				return ResponseEntity.ok(response);
			} else {
				response.put("status", "error");
				response.put("message", "Delete failed");
				return ResponseEntity.badRequest().body(response);
			}

		} catch (Exception e) {
			logger.error("deleteTemplate error", e);
			response.put("status", "error");
			response.put("message", "Server error while deleting template");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
