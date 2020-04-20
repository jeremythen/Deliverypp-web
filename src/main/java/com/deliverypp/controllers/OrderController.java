package com.deliverypp.controllers;

import com.deliverypp.models.Order;
import com.deliverypp.repositories.OrderRepository;
import com.deliverypp.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @GetMapping()
    public List<Order> getOrders() {

        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Order getOrders(@PathVariable int id) {

        return orderRepository.findById(id).orElseGet(null);
    }

    @PostMapping()
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> requestMap) {

        Map<String, Object> responseMap = orderService.createOrder(requestMap);

        return ResponseEntity.ok(responseMap);

    }

}
