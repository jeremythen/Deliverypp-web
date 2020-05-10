package com.deliverypp.services.order;

import com.deliverypp.controllers.OrderController;
import com.deliverypp.models.*;
import com.deliverypp.repositories.*;
import com.deliverypp.services.payment.StripeService;
import com.deliverypp.services.product.ProductService;
import com.deliverypp.services.stripe.StripeCustomerService;
import com.deliverypp.services.user.UserService;
import com.deliverypp.util.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
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

    @Autowired
    private StripeService stripeService;

    @Autowired
    private StripeCustomerService stripeCustomerService;

    @Transactional
    public DeliveryppResponse<List<Order>> getOrders() {

        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

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
    public DeliveryppResponse<?> createOrder(Map<String, Object> requestMap) {

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

            if(!userServiceResponse.isSuccess()) {
                response
                        .setStatus(ERROR)
                        .setSpecificStatus(USER_NOT_FOUND)
                        .setMessage("User not found.");
                return response;
            }

            User user = userServiceResponse.getResponse();

            Location location = new Location();

            double longitude = locationMap.get("longitude");
            double latitude = locationMap.get("latitude");

            location.setLatitude(latitude);

            location.setLongitude(longitude);

            location.setUser(user);

            locationRepository.save(location);

            Order order = new Order();

            order.setComment(comment);
            order.setStatus("ORDERED");
            order.setTotal(BigInteger.valueOf(total));
            order.setLocation(location);
            order.setUser(user);

            Order newOrder = orderRepository.save(order);

            for(Map<String, Integer> orderedProduct : orderedProducts) {
                int id = orderedProduct.get("id");
                int quantity = orderedProduct.get("quantity");

                DeliveryppResponse<Product> productServiceResponse = productService.getProductById(id);

                Product product = productServiceResponse.getResponse();

                OrderLine orderLine = new OrderLine();

                orderLine.setProduct(product);
                orderLine.setQuantity(quantity);
                orderLine.setOrder(newOrder);

                orderLineRepository.save(orderLine);

            }

            Map<String, Object> paymentParams = new HashMap<>();

            DeliveryppResponse<?> stripeCustomerResponse = stripeCustomerService.findByUser(user);

            if(!stripeCustomerResponse.isSuccess()) {
                response
                        .setStatus(ERROR)
                        .setMessage("Stripe Customer user not found by Deliverypp User.");
                return response;
            }

            StripeCustomer stripeCustomer = (StripeCustomer) stripeCustomerResponse.getResponse();

            paymentParams.put("amount", "" + total);
            paymentParams.put("customer", "" + total);
            paymentParams.put("stripeCustomerId", stripeCustomer.getStripeCustomerId());

            DeliveryppResponse<?> stripeServiceResponse = stripeService.makePayment(paymentParams);

            if(!stripeServiceResponse.isSuccess()) {
                response
                        .setStatus(ERROR)
                        .setMessage("Error making payment. " + stripeServiceResponse.getMessage());

                return response;
            }

            newOrder.setStatus("PAID");

            orderRepository.save(newOrder);

            response
                    .setStatus(SUCCESS)
                    .setMessage("Successfully created order.")
                    .setResponse(newOrder);

            return response;
        } else {

            DeliveryppResponse<List<Map<String, String>>> validationResponse = new DeliveryppResponse<>();

            List<Map<String, String>> validationList = (List<Map<String, String>>) paramValidation.get("validationList");

            logger.info("validationList: {}", validationList);
            logger.error("validationList: {}", validationList);

            validationResponse
                    .setStatus(ERROR)
                    .setSpecificStatus(ORDER_INVALID)
                    .setMessage("Order parameters are not valid.")
                    .setResponse(validationList);

            return validationResponse;
        }

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

            order.setUpdatedAt(LocalDateTime.now());

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

        order.setUpdatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        response
                .setStatus(SUCCESS)
                .setMessage("Order updated.")
                .setResponse(savedOrder);

        return response;

    }

    @Override
    public DeliveryppResponse<List<Order>> getUserOrders(User user) {

        List<Order> userOrders = orderRepository.findAllByUser(user);

        DeliveryppResponse<List<Order>> response = new DeliveryppResponse<>();

        for (Order userOrder : userOrders) {
            userOrder.setUser(null);
        }

        return response
                .setStatus(SUCCESS)
                .setMessage("User orders retrieved.")
                .setResponse(userOrders);

    }

    private Map<String, Object> validateParams(Map<String, Object> requestMap) {

        Map<String, Object> responseMap = new HashMap<>();

        List<Map<String, String>> validationList = new ArrayList<>();

        boolean valid = true;

        if(nonNull(requestMap)) {

            Object userIdObj = requestMap.get("userId");
            Object totalObj = requestMap.get("total");
            Object locationObj = requestMap.get("location");

            if(!(userIdObj instanceof Integer)) {
                Map<String, String> validateMap = new HashMap<>();
                valid = false;
                validationList.add(validateMap);
                validateMap.put("message", "userId not provided.");
                validateMap.put("code", "USER_INVALID_USER_ID");
            } else {
                int userId = (int) userIdObj;

                if(userId < 0) {
                    Map<String, String> validateMap = new HashMap<>();
                    valid = false;
                    validationList.add(validateMap);
                    validateMap.put("message", "Invalid userId");
                    validateMap.put("code", "USER_INVALID_USER_ID");
                }
            }

            if(!(totalObj instanceof Integer)) {
                Map<String, String> validateMap = new HashMap<>();
                valid = false;
                validationList.add(validateMap);
                validateMap.put("message", "total not provided.");
                validateMap.put("code", "ORDER_TOTAL_NOT_PROVIDED");
            } else {
                int total = (int) totalObj;

                if(total <= 0) {
                    Map<String, String> validateMap = new HashMap<>();
                    valid = false;
                    validationList.add(validateMap);
                    validateMap.put("message", "Total cannot be 0 or negative.");
                    validateMap.put("code", "ORDER_TOTAL_INVALID");
                }
            }

            if(!(locationObj instanceof Map)) {
                Map<String, String> validateMap = new HashMap<>();
                valid = false;
                validationList.add(validateMap);
                validateMap.put("message", "Locations not provided.");
                validateMap.put("code", "ORDER_LOCATION_NOT_PROVIDED");
            } else {
                Map<?, ?> locationMap = (Map<?, ?>) locationObj;

                Object longitudeObj = locationMap.get("longitude");
                Object latitudeObj = locationMap.get("latitude");

                if(!(longitudeObj instanceof Double)) {
                    Map<String, String> validateMap = new HashMap<>();
                    valid = false;
                    validationList.add(validateMap);
                    validateMap.put("message", "Location longitude required.");
                    validateMap.put("code", "ORDER_LOCATION_LONGITUDE_INVALID");
                }

                if(!(latitudeObj instanceof Double)) {
                    Map<String, String> validateMap = new HashMap<>();
                    valid = false;
                    validationList.add(validateMap);
                    validateMap.put("message", "Location latitude required.");
                    validateMap.put("code", "ORDER_LOCATION_LATITUDE_INVALID");
                }
            }

            if(requestMap.containsKey("comment") && !(requestMap.get("comment") instanceof String)) {
                Map<String, String> validateMap = new HashMap<>();
                valid = false;
                validationList.add(validateMap);
                validateMap.put("message", "Comment needs to be a string.");
                validateMap.put("code", "ORDER_COMMENT_INVALID");
            }

            Object orderedProductsObj = requestMap.get("products");

            if(!(orderedProductsObj instanceof List) || ((List<?>) orderedProductsObj).isEmpty()) {
                Map<String, String> validateMap = new HashMap<>();
                valid = false;
                validationList.add(validateMap);
                validateMap.put("message", "Products not provided.");
                validateMap.put("code", "ORDER_PRODUCTS_NOT_PROVIDED");
            } else {
                List<?> products = (List<?>) orderedProductsObj;

                for(Object productMapObj : products) {

                    if(!(productMapObj instanceof Map)) {
                        Map<String, String> validateMap = new HashMap<>();
                        valid = false;
                        validationList.add(validateMap);
                        validateMap.put("message", "Invalid product.");
                        validateMap.put("code", "ORDER_PRODUCT_INVALID");
                    }

                    Map<?, ?> productMap = (Map<?, ?>) productMapObj;

                    if(!(productMap.get("id") instanceof  Integer)) {
                        Map<String, String> validateMap = new HashMap<>();
                        valid = false;
                        validationList.add(validateMap);
                        validateMap.put("message", "Product id invalid.");
                        validateMap.put("code", "ORDER_PRODUCT_ID_INVALID");
                    } else {
                        int id = (Integer) productMap.get("id");

                        boolean productIsAvailable= productService.existsById(id);

                        if(!productIsAvailable) {
                            Map<String, String> validateMap = new HashMap<>();
                            valid = false;
                            validationList.add(validateMap);
                            validateMap.put("message", "Product with id " + id + " not available.");
                            validateMap.put("code", "ORDER_PRODUCT_ID_NOT_AVAILABLE");
                        }
                    }

                    if(!(productMap.get("quantity") instanceof  Integer)) {
                        Map<String, String> validateMap = new HashMap<>();
                        valid = false;
                        validationList.add(validateMap);
                        validateMap.put("message", "Product quantity invalid.");
                        validateMap.put("code", "ORDER_PRODUCT_QUANTITY_INVALID");
                    }

                    int quantity = (Integer) productMap.get("quantity");

                    if(quantity <= 0) {
                        Map<String, String> validateMap = new HashMap<>();
                        valid = false;
                        validationList.add(validateMap);
                        validateMap.put("message", "Product quantity cannot be 0 or negative.");
                        validateMap.put("code", "ORDER_PRODUCT_QUANTITY_0_NEGATIVE");
                    }

                }

                if(products.isEmpty()) {
                    Map<String, String> validateMap = new HashMap<>();
                    valid = false;
                    validationList.add(validateMap);
                    validateMap.put("message", "Products not provided.");
                    validateMap.put("code", "ORDER_PRODUCTS_NOT_PROVIDED");
                }
            }

        }

        responseMap.put("valid", valid);
        responseMap.put("validationList", validationList);

        return responseMap;

    }


}
