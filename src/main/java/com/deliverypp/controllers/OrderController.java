package com.deliverypp.controllers;

import com.deliverypp.models.Order;
import com.deliverypp.services.OrderService;
import com.deliverypp.util.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.deliverypp.util.DeliveryppResponse;

import javax.validation.Valid;

import static com.deliverypp.util.DeliveryppResponse.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @GetMapping()
    public ResponseEntity<DeliveryppResponse<?>> getOrders() {

        DeliveryppResponse<List<Order>> response = orderService.getOrders();

        return getDefaultResponse(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryppResponse<?>> getOrderById(@PathVariable int id) {

        DeliveryppResponse<Order> response = orderService.getOrderById(id);

        return getDefaultResponse(response);

    }

    @PostMapping()
    public ResponseEntity<DeliveryppResponse<?>> createOrder(@RequestBody Map<String, Object> requestMap) {

        DeliveryppResponse<?> response = orderService.createOrder(requestMap);

        return getDefaultResponse(response);

    }

    @PutMapping()
    public ResponseEntity<DeliveryppResponse<?>> updateOrder(@PathVariable int id, @Valid @RequestBody Order order) {

        DeliveryppResponse<?> response = orderService.updateOrder(order);

        return getDefaultResponse(response);

    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<DeliveryppResponse<?>> setStatus(@PathVariable int id, @PathVariable String status) {

        DeliveryppResponse<Order> response = orderService.updateStatus(id, status);

        return getDefaultResponse(response);

    }

}
