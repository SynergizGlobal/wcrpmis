package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.DTO.Messages;
import com.wcr.wcrbackend.repo.IMessageRepository;

@Service
public class MessageService implements IMessageService {

	@Autowired
	private IMessageRepository messageRepository;
	@Override
	public List<Messages> getMessages(Messages mObj) throws Exception {
		// TODO Auto-generated method stub
		return messageRepository.getMessages(mObj);
	}

}
