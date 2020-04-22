package com.deliverypp.controllers;

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
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@GetMapping()
	public Iterable<User> getUsers() {

		Iterable<User> users = userService.getUsers();

		return users;

	}

	@PutMapping()
	public ResponseEntity.HeadersBuilder updateUser(@Valid @RequestBody User user) {

		userService.save(user);
		return ResponseEntity.noContent();

	}

	@GetMapping("/{userName}")
	public ResponseEntity getUserByUserName(@PathVariable String userName) {

		User user = userService.findByUsername(userName);

		if(Objects.isNull(user)) {
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("message", "No user with username " + userName + " was found");
			ResponseEntity.badRequest().body(responseMap);
		}

		Map<String, Object> userInfo = userService.getFilteredUser(user);

		return ResponseEntity.ok(userInfo);

	}

	@PostMapping("/{userName}/role")
	public ResponseEntity addUserRole(@PathVariable String userName, @RequestBody Map<String, String> requestMap) {

		String newRole = requestMap.get("role");

		logger.info("New role: " + newRole);

		boolean isValidRole = Arrays.stream(Roles.values()).anyMatch(role -> role.name().equals(newRole));

		logger.info("isValidRole: " + isValidRole);

		logger.info("Roles.values: " + Arrays.toString(Roles.values()));

		if(!isValidRole) {
			logger.info("!isValidRole: " + isValidRole);
			Map<String, Object> responseBody = new HashMap<>();

			responseBody.put("message", "Invalid role.");
			responseBody.put("rolesAllowed", Roles.values());

			return ResponseEntity.badRequest().body(responseBody);

		}

		User user = userService.findByUsername(userName);

		user.setRole(newRole);

		userService.save(user);

		return ResponseEntity.noContent().build();

	}

	@DeleteMapping("/{userName}/role/{roleName}")
	public ResponseEntity.HeadersBuilder removeUserRole(@PathVariable String userName, @PathVariable String roleName) {

		User user = userService.findByUsername(userName);

		user.setRole(Roles.USER.name());

		userService.save(user);

		return ResponseEntity.noContent();

	}

}
