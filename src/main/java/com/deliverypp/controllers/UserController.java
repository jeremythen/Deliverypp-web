package com.deliverypp.controllers;

import com.deliverypp.util.DeliveryppResponse;
import com.deliverypp.util.Roles;
import com.deliverypp.models.User;
import com.deliverypp.security.JwtTokenProvider;
import com.deliverypp.services.user.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static com.deliverypp.util.DeliveryppResponse.*;
import static com.deliverypp.util.DeliveryppResponseStatus.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@GetMapping()
	public ResponseEntity<?> getUsers() {

		Iterable<User> users = userService.getUsers();

		DeliveryppResponse<?> response = DeliveryppResponse.<Iterable<User>>newResponse()
				.setStatus(SUCCESS)
				.setMessage("Successfully retried users.")
				.setResponse(users);

		return ResponseEntity.ok(response);

	}

	@PutMapping()
	public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {

		userService.save(user);

		DeliveryppResponse<?> response = DeliveryppResponse.newResponse()
				.setStatus(SUCCESS)
				.setSpecificStatus(USER_UPDATED)
				.setMessage("Successfully updated users.");

		return ResponseEntity.ok(response);

	}

	@GetMapping("/{userName}")
	public ResponseEntity<?> getUserByUserName(@PathVariable String userName) {

		User user = userService.findByUsername(userName);

		DeliveryppResponse<User> response = DeliveryppResponse.newResponse();

		if(Objects.isNull(user)) {
			response.setStatus(ERROR)
					.setMessage("No user with username " + userName + " was found")
					.setSpecificStatus(USER_NOT_FOUND);

			return ResponseEntity.badRequest().body(response);
		}

		response.setStatus(SUCCESS)
				.setMessage("Successfully retrieved user.")
				.setResponse(user);

		return ResponseEntity.ok(response);

	}

	@PostMapping("/{userName}/role")
	public ResponseEntity<?> addUserRole(@PathVariable String userName, @RequestBody Map<String, String> requestMap) {

		String newRole = requestMap.get("role");

		logger.info("New role: " + newRole);

		boolean isValidRole = Roles.isValidRole(newRole);

		logger.info("isValidRole: " + isValidRole);

		logger.info("Roles.values: " + Arrays.toString(Roles.values()));

		DeliveryppResponse<User> response = DeliveryppResponse.newResponse();

		if(!isValidRole) {
			logger.info("!isValidRole: " + isValidRole);
			Map<String, Object> responseBody = new HashMap<>();

			responseBody.put("message", "Invalid role.");
			responseBody.put("rolesAllowed", Roles.values());

			response.setStatus(ERROR)
					.setMessage("Invalid role")
					.setSpecificStatus(USER_INVALID_ROLE);

			return ResponseEntity.badRequest().body(response);

		}

		User user = userService.findByUsername(userName);

		user.setRole(newRole);

		userService.save(user);

		response.setStatus(SUCCESS)
				.setMessage("User role updated.")
				.setResponse(user);

		return ResponseEntity.ok(response);

	}

}
