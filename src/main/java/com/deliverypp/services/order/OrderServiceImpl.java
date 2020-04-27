package com.deliverypp.services.order;

import com.deliverypp.controllers.OrderController;
import com.deliverypp.models.*;
import com.deliverypp.repositories.*;
import com.deliverypp.services.product.ProductService;
import com.deliverypp.services.user.UserService;
import com.deliverypp.util.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;

import static java.util.Objects.*;

import com.deliverypp.util.DeliveryppResponse;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

import static com.deliverypp.util.DeliveryppResponse.*;
import static com.deliverypp.util.DeliveryppResponseStatus.*;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private ProductService productService;

    @Transactional
    public DeliveryppResponse<List<Order>> getOrders() {

        List<Order> orders = orderRepository.findAll();

        DeliveryppResponse<List<Order>> response = new DeliveryppResponse<>();

        response
                .setStatus(SUCCESS)
                .setMessage("Success retrieving orders.")
                .setResponse(orders);

        return response;
    }

    @Transactional
    public DeliveryppResponse<Order> getOrderById(int id) {

        Optional<Order> optionalOrder = orderRepository.findById(id);

        DeliveryppResponse<Order> response = new DeliveryppResponse<>();

        if(optionalOrder.isPresent()) {
            response
                    .setStatus(SUCCESS)
                    .setMessage("Successfully retrieved order with id: " + id)
                    .setResponse(optionalOrder.get());
        } else {
            response.setStatus(ERROR)
                    .setSpecificStatus(ORDER_NOT_FOUND)
                    .setMessage("No order with id: " + id);
        }

        return response;

    }

    @Transactional
    public DeliveryppResponse<Order> createOrder(Map<String, Object> requestMap) {

        logger.info("createOrder: " + requestMap);

        DeliveryppResponse<Order> response = new DeliveryppResponse<>();

        Map<String, Object> paramValidation = validateParams(requestMap);

        boolean paramsAreValid = (boolean) paramValidation.get("valid");

        if(paramsAreValid) {

            int userId = (int) requestMap.get("userId");
            int total = (int) requestMap.get("total");

            Map<String, Double> locationMap = (Map<String, Double>) requestMap.get("location");

            String comment = (String) requestMap.get("comment");

            List<Map<String, Integer>> orderedProducts = (List<Map<String, Integer>>) requestMap.get("products");

            DeliveryppResponse<User> userServiceResponse = userService.findUserById(userId);

            User user = null;

            if(userServiceResponse.isSuccess()) {
                user = userServiceResponse.getResponse();
            } else {
                response
                        .setStatus(ERROR)
                        .setSpecificStatus(USER_NOT_FOUND)
                        .setMessage("User not found.");
            }

            Location location = new Location();

            Double longitude = locationMap.get("longitude");
            Double latitude = locationMap.get("latitude");

            if(isNull(latitude) || isNull(longitude)) {
                response
                        .setStatus(ERROR)
                        .setSpecificStatus(LOCATION_INVALID)
                        .setMessage("Longitude and Latitude required.");

                return response;
            }

            location.setLatitude(latitude);

            location.setLatitude(longitude);

            location.setUser(user);

            locationRepository.save(location);

            Order order = new Order();

            order.setComment(comment);
            order.setStatus("ordered");
            order.setTotal(BigInteger.valueOf(total));
            order.setLocation(location);
            order.setUser(user);

            Order newOrder = orderRepository.save(order);

            if(orderedProducts.size() == 0) {
                response
                        .setStatus(ERROR)
                        .setSpecificStatus(PRODUCT_EMPTY)
                        .setMessage("No products selected.");
            }

            for(Map<String, Integer> orderedProduct : orderedProducts) {
                int id = orderedProduct.get("id");
                int quantity = orderedProduct.get("quantity");

                DeliveryppResponse<Product> productServiceResponse = productService.getProductById(id);

                Product product = null;

                if(SUCCESS.equals(productServiceResponse.getStatus())) {
                    product = productServiceResponse.getResponse();
                } else {
                    response
                            .setStatus(ERROR)
                            .setSpecificStatus(PRODUCT_NOT_AVAILABLE)
                            .setMessage("Product not available.");
                    return response;
                }

                OrderLine orderLine = new OrderLine();

                orderLine.setProduct(product);
                orderLine.setQuantity(quantity);
                orderLine.setOrder(newOrder);

                orderLineRepository.save(orderLine);

            }

            response
                    .setStatus(SUCCESS)
                    .setMessage("Successfully created order.")
                    .setResponse(newOrder);

            return response;
        } else {

            response
                    .setStatus(ERROR)
                    .setSpecificStatus(ORDER_INVALID)
                    .setMessage("Order parameters are not valid.");

        }

        return response;

    }

    @Transactional
    public DeliveryppResponse<Order> updateStatus(int orderId, String status) {

        DeliveryppResponse<Order> response = new DeliveryppResponse<>();

        boolean isValidStatus = OrderStatus.isValidStatus(status);

        if(!isValidStatus) {
            response
                    .setStatus(ERROR)
                    .setSpecificStatus(ORDER_STATUS_INVALID)
                    .setMessage("Status not valid.");
        }

        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if(optionalOrder.isPresent()) {

            Order order = optionalOrder.get();

            order.setStatus(status);

            orderRepository.save(order);

            response
                    .setStatus(SUCCESS)
                    .setMessage("Order updated")
                    .setResponse(order);

        } else {
            response
                    .setStatus(ERROR)
                    .setSpecificStatus(ORDER_NOT_FOUND)
                    .setMessage("Order not found")
                    .setResponse(null);
        }

        return response;

    }

    @Transactional
    public DeliveryppResponse<Order> updateOrder(@Valid Order order) {

        DeliveryppResponse<Order> response = new DeliveryppResponse<>();

        Order savedOrder = orderRepository.save(order);

        response
                .setStatus(SUCCESS)
                .setMessage("Order updated.")
                .setResponse(savedOrder);

        return response;

    }

    private Map<String, Object> validateParams(Map<String, Object> requestMap) {

        // TODO: Check if all the parameters are valid.

        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("valid", true);

        return responseMap;

    }


}
