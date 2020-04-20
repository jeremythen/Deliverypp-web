package com.deliverypp.services;

import com.deliverypp.controllers.OrderController;
import com.deliverypp.models.*;
import com.deliverypp.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.*;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private ProductRepository productRepository;

    public Map<String, Object> createOrder(Map<String, Object> requestMap) {

        logger.info("createOrder: " + requestMap);

        Map<String, Object> responseMap = new HashMap<>();

        Map<String, Object> paramValidation = validateParams(requestMap);

        boolean paramsAreValid = (boolean) paramValidation.get("valid");

        if(paramsAreValid) {

            int userId = (int) requestMap.get("userId");
            int total = (int) requestMap.get("total");

            Map<String, Double> locationMap = (Map<String, Double>) requestMap.get("location");

            String comment = (String) requestMap.get("comment");

            List<Map<String, Integer>> orderedProducts = (List<Map<String, Integer>>) requestMap.get("products");

            Optional<User> optionalUser = userRepository.findById(userId);

            User user = optionalUser.get();

            Location location = new Location();

            location.setLatitude(locationMap.get("latitude"));

            location.setLatitude(locationMap.get("longitude"));

            location.setUser(user);

            locationRepository.save(location);

            Order order = new Order();

            order.setComment(comment);
            order.setStatus("ordered");
            order.setTotal(BigInteger.valueOf(total));
            order.setLocation(location);
            order.setUser(user);

            Order newOrder = orderRepository.save(order);

            if(isNull(newOrder)) {
                logger.error("newOrder was not saved.");
                // TODO: Return an error, order cannot proceed anymore.
            }

            orderedProducts.forEach(orderedProduct -> {

                int id = orderedProduct.get("id");
                int quantity = orderedProduct.get("quantity");

                Optional<Product> optionalProduct = productRepository.findById(id);

                Product product = optionalProduct.get();

                OrderLine orderLine = new OrderLine();

                orderLine.setProduct(product);
                orderLine.setQuantity(quantity);
                orderLine.setOrder(newOrder);

                OrderLine newOrderLine = orderLineRepository.save(orderLine);

                if(isNull(newOrderLine)) {
                    logger.error("newOrderLine was not saved.");
                    // TODO: Return an error, order cannot proceed anymore.
                }

            });


            responseMap.put("order", newOrder);

        }

        return responseMap;

    }

    private Map<String, Object> validateParams(Map<String, Object> requestMap) {

        // TODO: Check if all the parameters are valid.

        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("valid", true);

        return responseMap;

    }


}
