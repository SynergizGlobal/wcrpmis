package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.WebLinks;

public interface IWebLinksRepository {

	List<WebLinks> getWebLinks();

	List<WebLinks> getWebLinks(WebLinks obj) throws Exception;

	boolean updateWebLinks(WebLinks obj) throws Exception;

}
