package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.WebDocuments;
import com.wcr.wcrbackend.repo.IDocumentRepository;

@Service
public class DocumentService implements IDocumentService {

	@Autowired
	private IDocumentRepository documentRepository;
	@Override
	public List<WebDocuments> getDocumentTypes() {
		// TODO Auto-generated method stub
		return documentRepository.getDocumentTypes();
	}

}
