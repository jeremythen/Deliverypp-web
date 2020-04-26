package com.deliverypp.services.user;

import com.deliverypp.models.User;
import com.deliverypp.repositories.UserRepository;

import com.deliverypp.util.DeliveryppResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.deliverypp.util.DeliveryppResponseStatus.*;
import static com.deliverypp.util.DeliveryppResponse.*;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@Override
	public DeliveryppResponse<User> save(User user) {

		DeliveryppResponse<User> response = new DeliveryppResponse<>();

		if(userRepository.existsByEmail(user.getEmail())) {
			response.setStatus(DeliveryppResponse.ERROR)
					.setSpecificStatus(USER_EMAIL_TAKEN)
					.setMessage("Email already taken.");
			return response;
		}

		if(userRepository.existsByUsername(user.getUsername())) {
			response.setStatus(DeliveryppResponse.ERROR)
					.setSpecificStatus(USER_NAME_TAKEN)
					.setMessage("Username already taken.");
			return response;
		}

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRole("USER");
		userRepository.save(user);

		response.setStatus(DeliveryppResponse.SUCCESS)
				.setMessage("User registered successfully.")
				.setResponse(user);

		return response;
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public DeliveryppResponse<User> findUserById(int id) {

		Optional<User> optionalUser = userRepository.findById(id);

		DeliveryppResponse<User> response = new DeliveryppResponse<>();

		if(optionalUser.isPresent()) {
			response
					.setStatus(SUCCESS)
					.setMessage("User retrieved successfully")
					.setResponse(optionalUser.get());
			return response;
		} else {
			response
					.setStatus(ERROR)
					.setSpecificStatus(USER_NOT_FOUND)
					.setMessage("Not a valid user.")
					.setResponse(optionalUser.get());
			return response;
		}

	}


}
