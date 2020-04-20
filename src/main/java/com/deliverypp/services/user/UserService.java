package com.deliverypp.services.user;

import com.deliverypp.models.User;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {

    ResponseEntity save(User user);

    User findByUsername(String username);

    Map<String, Object> getFilteredUser(User user);

}
