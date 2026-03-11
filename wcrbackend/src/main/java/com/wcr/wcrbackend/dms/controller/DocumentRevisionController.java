package com.wcr.wcrbackend.dms.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.dms.repository.DocumentRevisionRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/documentversion")
@RequiredArgsConstructor
public class DocumentRevisionController {

    private final DocumentRevisionRepository documentRevisionRepository;
	
	@GetMapping("/get")
	List<com.wcr.wcrbackend.dms.dto.DocumentRevisionDTO> getAllVersions(@RequestParam("fileNumber") String fileNumber
			, @RequestParam("fileName") String fileName) {
		return documentRevisionRepository.findAllByFileNumberAndFileName(fileNumber, fileName);
	}

}
