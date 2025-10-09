package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.WebDocuments;

public interface IDocumentRepository {

	List<WebDocuments> getDocumentTypes();

}
