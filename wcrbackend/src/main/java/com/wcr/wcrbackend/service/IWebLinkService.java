package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.WebLinks;

public interface IWebLinkService {

	public List<WebLinks> getWebLinks();

	public List<WebLinks> getWebLinks(WebLinks obj) throws Exception;

	public boolean updateWebLinks(WebLinks obj) throws Exception;

}
