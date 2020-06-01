package com.deliverypp.controllers;

import com.deliverypp.models.User;
import com.deliverypp.security.JwtTokenProvider;
import com.deliverypp.services.user.UserService;
import com.deliverypp.util.DeliveryppResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.deliverypp.util.DeliveryppResponse.*;
import static com.deliverypp.util.DeliveryppResponseStatus.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {

        String password = user.getPassword();
        String username = user.getUsername();

        logger.info("register user {}", user);

        DeliveryppResponse<?> response = userService.save(user);

        if(!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }

        DeliveryppResponse<?> loginResponse = login(username, password);

        if(loginResponse.isSuccess()) {
            DeliveryppResponse<Object> newResponse = DeliveryppResponse.newResponse();
            newResponse.setStatus(response.getStatus());
            newResponse.setMessage(response.getMessage());
            newResponse.setResponse(loginResponse.getResponse());
            return ResponseEntity.ok(newResponse);
        }

        return ResponseEntity.ok(response);

    }

    private DeliveryppResponse<?> login(String username, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        int userId = tokenProvider.getUserIdFromJWT(jwt);

        DeliveryppResponse<User> userResponse = userService.findUserById(userId);

        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("token", jwt);
        responseMap.put("user", userResponse.getResponse());

        DeliveryppResponse<?> response = DeliveryppResponse.newResponse()
                .setStatus(DeliveryppResponse.SUCCESS)
                .setMessage("User logged in successfully")
                .setResponse(responseMap);

        return response;

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> userMap) {

        logger.info("login username: {}", userMap.get("username"));

        String username =userMap.get("username");
        String password = userMap.get("password");

        DeliveryppResponse<?> response = login(username, password);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/auth/{token}")
    public ResponseEntity<?> getUserByToken(@PathVariable String token) {

        logger.info("getUserByToken");

        DeliveryppResponse<User> response;

        if(StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            int userId = tokenProvider.getUserIdFromJWT(token);
            response = userService.findUserById(userId);
            return ResponseEntity.ok(response);
        } else {
            response  = new DeliveryppResponse<>();
            response
                    .setStatus(ERROR)
                    .setMessage("Invalid credentials")
                    .setSpecificStatus(USER_INVALID_CRED);
            return ResponseEntity.badRequest().body(response);
        }

    }

}
