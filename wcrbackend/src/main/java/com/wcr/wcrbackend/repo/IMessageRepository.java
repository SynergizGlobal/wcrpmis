package com.wcr.wcrbackend.repo;

import java.util.List;

import com.wcr.wcrbackend.DTO.Messages;

public interface IMessageRepository {

	List<Messages> getMessages(Messages mObj) throws Exception;

}
