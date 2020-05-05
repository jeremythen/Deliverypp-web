package com.deliverypp.controllers;

import com.deliverypp.models.User;
import com.deliverypp.services.payment.StripeService;
import com.deliverypp.services.user.UserService;
import com.deliverypp.util.DeliveryppResponse;
import static com.deliverypp.util.DeliveryppResponseStatus.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private StripeService stripeService;
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    public PaymentController(StripeService stripeService, UserService userService) {
        this.stripeService = stripeService;
        this.userService = userService;
    }

    @PostMapping("/customer")
    public ResponseEntity<?> createStripeCustomer(@RequestBody Map<String, Object> requestMap) {

        logger.info("createStripeCustomer requestMap: {}", requestMap);

        Object userIdObj = requestMap.get("userId");

        if(!(userIdObj instanceof Integer)) {
            DeliveryppResponse<?> response = DeliveryppResponse.newResponse()
                    .setStatus(DeliveryppResponse.ERROR)
                    .setMessage("userId not provided")
                    .setSpecificStatus(USER_ID_NOT_PROVIDED);
            return ResponseEntity.badRequest().body(response);
        }

        int userId = (int) userIdObj;
        DeliveryppResponse<User> userResponse = userService.findUserById(userId);

        if(!userResponse.isSuccess()) {
            logger.info("createStripeCustomer User not found.");
            DeliveryppResponse<?> response = DeliveryppResponse.newResponse()
                    .setStatus(DeliveryppResponse.ERROR)
                    .setMessage("User not found.")
                    .setSpecificStatus(USER_NOT_FOUND);
            return ResponseEntity.badRequest().body(response);
        }

        User user = userResponse.getResponse();
        DeliveryppResponse<?> stripeResponse = stripeService.createStripeCustomer(user);
        logger.info("createStripeCustomer stripeResponse: {}", stripeResponse);
        if(stripeResponse.isSuccess()) {
            return ResponseEntity.ok(stripeResponse);
        } else {
            return ResponseEntity.badRequest().body(stripeResponse);
        }

    }

    @GetMapping("/customer/{stripeUserId}")
    public ResponseEntity<?> getStripeCustomer(@PathVariable String stripeUserId) {

        DeliveryppResponse<?> response = stripeService.getCustomer(stripeUserId);

        if(response.isSuccess()) {
            return  ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }

    }

    @PostMapping("/card")
    public ResponseEntity<?> addCard(@RequestBody Map<String, Object> requestMap) {

        DeliveryppResponse<?> response = stripeService.addCard(requestMap);

        if(response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }

    }

    @PostMapping()
    public ResponseEntity<?> makePayment(@RequestBody Map<String, Object> requestMap) {

        DeliveryppResponse<?> response = stripeService.makePayment(requestMap);

        if(response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }

    }

}
