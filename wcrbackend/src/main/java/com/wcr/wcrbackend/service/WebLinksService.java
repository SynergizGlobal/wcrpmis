package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.WebLinks;
import com.wcr.wcrbackend.repo.IWebLinksRepository;

import jakarta.transaction.Transactional;

@Service
public class WebLinksService implements IWebLinkService {

	@Autowired
	IWebLinksRepository weblinksRepository;
	@Override
	public List<WebLinks> getWebLinks() {
		// TODO Auto-generated method stub
		return weblinksRepository.getWebLinks();
	}
	@Override
	public List<WebLinks> getWebLinks(WebLinks obj) throws Exception {
		// TODO Auto-generated method stub
		return weblinksRepository.getWebLinks(obj);
	}
	@Override
	@Transactional
	public boolean updateWebLinks(WebLinks obj) throws Exception {
		// TODO Auto-generated method stub
		return weblinksRepository.updateWebLinks(obj);
	}

}
