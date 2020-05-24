package com.deliverypp.services.order;

import com.deliverypp.controllers.OrderController;
import com.deliverypp.models.*;
import com.deliverypp.repositories.*;
import com.deliverypp.services.payment.StripeService;
import com.deliverypp.services.product.ProductService;
import com.deliverypp.services.stripe.StripeCustomerService;
import com.deliverypp.services.user.UserService;
import com.deliverypp.util.DeliveryppLoggin;
import com.deliverypp.util.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.*;

import com.deliverypp.util.DeliveryppResponse;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

import static com.deliverypp.util.DeliveryppResponse.*;
import static com.deliverypp.util.DeliveryppResponseStatus.*;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private LocationRepository locationRepository;

    private UserService userService;

    private OrderRepository orderRepository;

    private OrderLineRepository orderLineRepository;

    private ProductService productService;

    private StripeService stripeService;

    private StripeCustomerService stripeCustomerService;

    @Autowired
    public OrderServiceImpl(LocationRepository locationRepository, UserService userService, OrderRepository orderRepository,
                            OrderLineRepository orderLineRepository, ProductService productService, StripeService stripeService,
                            StripeCustomerService stripeCustomerService) {

        this.locationRepository = locationRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
        this.productService = productService;
        this.stripeService = stripeService;
        this.stripeCustomerService = stripeCustomerService;
    }

    @Transactional
    @Override
    public DeliveryppResponse<List<Order>> getOrders() {

        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        DeliveryppResponse<List<Order>> response = new DeliveryppResponse<>();

        response
                .setStatus(SUCCESS)
                .setResponse(orders);

        return response;
    }

    @Transactional
    @Override
    public DeliveryppResponse<Order> getOrderById(int id) {

        Optional<Order> optionalOrder = orderRepository.findById(id);

        DeliveryppResponse<Order> response = new DeliveryppResponse<>();

        if(optionalOrder.isPresent()) {
            response
                    .setStatus(SUCCESS)
                    .setResponse(optionalOrder.get());
        } else {
            response.setStatus(ERROR)
                    .setSpecificStatus(ORDER_NOT_FOUND)
                    .setMessage("No order with id: " + id);
        }

        return response;

    }

    @Transactional
    @Override
    @DeliveryppLoggin
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
                    .setResponse(order);

        } else {
            response
                    .setStatus(ERROR)
                    .setSpecificStatus(ORDER_NOT_FOUND)
                    .setMessage("Order not found");
        }

        return response;

    }

    @Transactional
    @Override
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
                .setResponse(userOrders);

    }

    @Transactional
    @Override
    @DeliveryppLoggin
    public DeliveryppResponse<?> createOrder(Map<String, Object> requestMap) {

        logger.info("createOrder: " + requestMap);

        DeliveryppResponse<Order> response = new DeliveryppResponse<>();

        Map<String, Object> paramValidation = validateParams(requestMap);

        boolean paramsAreValid = (boolean) paramValidation.get("valid");

        if(!paramsAreValid) {
            return getValidationErrorResponse(paramValidation);
        }

        int userId = (int) requestMap.get("userId");

        DeliveryppResponse<User> userServiceResponse = userService.findUserById(userId);

        if(!userServiceResponse.isSuccess()) {
            return userServiceResponse;
        }

        User user = userServiceResponse.getResponse();

        Location location = getLocation(requestMap);

        location.setUser(user);

        locationRepository.save(location);

        String comment = (String) requestMap.get("comment");

        Order order = getNewOrder(comment, user, location);

        List<OrderLine> orderLines = getOrderLines(requestMap);

        saveOrderLines(order, orderLines);

        int totalAmount = getTotalAmount(orderLines);

        order.setTotal(totalAmount);

        orderRepository.save(order);

        DeliveryppResponse<?> paymentResponse = handlePayment(user, totalAmount);

        if(!paymentResponse.isSuccess()) {
            return paymentResponse;
        }

        order.setStatus("PAID");

        orderRepository.save(order);

        response
                .setStatus(SUCCESS)
                .setResponse(order);

        return response;

    }

    @DeliveryppLoggin
    private DeliveryppResponse<?> handlePayment(User user, int totalAmount) {

        DeliveryppResponse<?> response = new DeliveryppResponse<>();

        Map<String, Object> paymentParams = new HashMap<>();

        DeliveryppResponse<?> stripeCustomerResponse = stripeCustomerService.findByUser(user);

        if(!stripeCustomerResponse.isSuccess()) {
            response
                    .setStatus(ERROR)
                    .setMessage("Stripe Customer user not found by Deliverypp User.");
            return response;
        }

        StripeCustomer stripeCustomer = (StripeCustomer) stripeCustomerResponse.getResponse();

        paymentParams.put("amount", "" + totalAmount);
        paymentParams.put("stripeCustomerId", stripeCustomer.getStripeCustomerId());

        DeliveryppResponse<?> stripeServiceResponse = stripeService.makePayment(paymentParams);

        if(!stripeServiceResponse.isSuccess()) {
            response
                    .setStatus(ERROR)
                    .setMessage("Error making payment. " + stripeServiceResponse.getMessage());

            return response;
        }

        return response.setStatus(SUCCESS);

    }

    private Order getNewOrder(String comment, User user, Location location) {
        Order order = new Order();
        order.setComment(comment);
        order.setStatus("ORDERED");
        order.setLocation(location);
        order.setUser(user);
        return order;
    }

    private int getTotalAmount(List<OrderLine> orderLines) {
        return orderLines.stream()
                    .mapToInt(OrderLine::getTotal)
                    .sum();
    }

    private void saveOrderLines(Order order, List<OrderLine> orderLines) {
        orderLines.forEach(orderLine -> {
            orderLine.setOrder(order);
            orderLineRepository.save(orderLine);
        });
    }

    @DeliveryppLoggin
    private List<OrderLine> getOrderLines(Map<String, Object> requestMap) {

        List<Map<String, Integer>> orderedProductsMaps = (List<Map<String, Integer>>) requestMap.get("products");

        List<OrderLine> orderLines = orderedProductsMaps.stream().map(productMap -> {
            int id = productMap.get("id");
            int quantity = productMap.get("quantity");

            DeliveryppResponse<Product> productServiceResponse = productService.getProductById(id);

            Product product = productServiceResponse.getResponse();

            OrderLine orderLine = new OrderLine();

            orderLine.setProduct(product);
            orderLine.setQuantity(quantity);
            orderLine.setTotal(quantity * product.getPrice());

            return orderLine;

        }).collect(Collectors.toList());

        return orderLines;

    }

    private DeliveryppResponse<?> getValidationErrorResponse(Map<String, Object> paramValidation) {
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

    private Location getLocation(Map<String, Object> requestMap) {

        Map<String, Double> locationMap = (Map<String, Double>) requestMap.get("location");

        Location location = new Location();

        double longitude = locationMap.get("longitude");
        double latitude = locationMap.get("latitude");

        location.setLatitude(latitude);

        location.setLongitude(longitude);
        return location;
    }

    private Map<String, Object> validateParams(Map<String, Object> requestMap) {

        Map<String, Object> responseMap = new HashMap<>();

        List<Map<String, String>> validationList = new ArrayList<>();

        boolean valid = true;

        if(nonNull(requestMap)) {

            Object userIdObj = requestMap.get("userId");
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
