package com.deliverypp.services.user;

import com.deliverypp.models.User;
import com.deliverypp.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public ResponseEntity save(User user) {

		Map<String, Object> responseMap = new HashMap<>();

		if(userRepository.existsByEmail(user.getEmail())) {
			responseMap.put("message", "Email is already taken.");
			responseMap.put("ERROR_CODE", "EMAIL_TAKEN");
			return ResponseEntity.badRequest().body(responseMap);
		}

		if(userRepository.existsByUsername(user.getUsername())) {
			responseMap.put("message", "Username is already taken.");
			responseMap.put("ERROR_CODE", "USERNAME_TAKEN");
			return ResponseEntity.badRequest().body(responseMap);
		}

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRole("USER");
		userRepository.save(user);

		responseMap = getFilteredUser(user);

		return ResponseEntity.ok(responseMap);
	}

	@Override
	public Map<String, Object> getFilteredUser(User user) {

		Map<String, Object> userInfo = new HashMap<>();

		userInfo.put("id", user.getId());
		userInfo.put("name", user.getName());
		userInfo.put("lastName", user.getLastName());
		userInfo.put("username", user.getUsername());
		userInfo.put("email", user.getEmail());
		userInfo.put("role", user.getRole());

		return userInfo;

	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
