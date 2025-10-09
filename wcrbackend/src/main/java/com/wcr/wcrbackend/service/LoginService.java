package com.wcr.wcrbackend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.config.EncryptDecrypt;
import com.wcr.wcrbackend.entity.User;
import com.wcr.wcrbackend.repo.LoginRepository;

@Service
public class LoginService implements ILoginService {

	@Autowired
	private LoginRepository loginRepo;

	@Override
	public User authenticate(String userId, String password) throws Exception {
		Optional<User> userOpt = loginRepo.findById(userId);

		if (!userOpt.isPresent()) {
			throw new Exception("Invalid userId or password");
		}

		User user = userOpt.get();
		
		User matchedUser = null;
		// for (User user : userList) {
		boolean isEncrypted = "true".equalsIgnoreCase(user.getIsPasswordEncrypted());
		try {
			if (isEncrypted) {
				String encryptedInputPassword = EncryptDecrypt.encrypt(password);
				if (user.getPassword().equals(encryptedInputPassword)) {
					matchedUser = user;
				}
			} else {
				if (user.getPassword().equals(password)) {
					matchedUser = user;
				}
			}
		} catch (Exception e) {
			throw new Exception("Encryption error");
		}
		// }

		if (matchedUser == null) {
			throw new Exception("Invalid userId or password");
		}

		return matchedUser;
	}

	@Override
	public void updateSessionId(String userId, String sessionId) {
		Optional<User> optionalUser = loginRepo.findById(userId);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setSingleLoginSessionId(sessionId);
			loginRepo.save(user);
		}
	}

}
