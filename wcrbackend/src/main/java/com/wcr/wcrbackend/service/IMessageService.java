package com.wcr.wcrbackend.service;

import java.util.List;

import com.wcr.wcrbackend.DTO.Messages;

public interface IMessageService {

	List<Messages> getMessages(Messages mObj) throws Exception;

}
