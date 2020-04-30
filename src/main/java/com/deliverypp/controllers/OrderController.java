package com.deliverypp.controllers;

import com.deliverypp.models.Order;
import com.deliverypp.services.order.OrderService;
import com.deliverypp.services.order.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.deliverypp.util.DeliveryppResponse;

import javax.validation.Valid;

import static com.deliverypp.util.DeliveryppResponse.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public ResponseEntity<?> getOrders() {

        logger.info("getOrders");

        DeliveryppResponse<List<Order>> response = orderService.getOrders();

        return getDefaultResponse(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable int id) {

        logger.info("getOrderById {}", id);

        DeliveryppResponse<Order> response = orderService.getOrderById(id);

        return getDefaultResponse(response);

    }

    @PostMapping()
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> requestMap) {

        logger.info("createOrder requestMap {}", requestMap);

        DeliveryppResponse<?> response = orderService.createOrder(requestMap);

        if(response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }

    }

    @PutMapping()
    public ResponseEntity<?> updateOrder(@Valid @RequestBody Order order) {

        DeliveryppResponse<Order> oldOrderResponse = orderService.getOrderById(order.getId());

        if(oldOrderResponse.isSuccess()) {
            logger.info("updateOrder old order {}", oldOrderResponse.getResponse());
        }

        logger.info("updateOrder new order {}", order);

        DeliveryppResponse<?> response = orderService.updateOrder(order);

        return getDefaultResponse(response);

    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<?> setStatus(@PathVariable int id, @PathVariable String status) {

        DeliveryppResponse<Order> orderResponse = orderService.getOrderById(id);

        if(orderResponse.isSuccess()) {
            logger.info("setStatus old status {}", orderResponse.getResponse().getStatus());
        }

        logger.info("setStatus new status {}", status);

        DeliveryppResponse<Order> response = orderService.updateStatus(id, status);

        return getDefaultResponse(response);

    }

}
