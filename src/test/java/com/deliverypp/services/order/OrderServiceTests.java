package com.deliverypp.services.order;

import com.deliverypp.models.Order;
import com.deliverypp.models.User;
import com.deliverypp.util.DeliveryppResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE) // Just load what is needed. No @Controllers will load.
@SpringBootTest
public class OrderServiceTests {

    @Autowired
    private OrderService orderService;

    @Test
    public void getting_orders() {

        DeliveryppResponse<?> orderServiceResponse = orderService.getOrders();

        Assertions.assertTrue(orderServiceResponse.isSuccess(), "Failed to get orders.");

    }

    @Test
    public void create_order() {

        Map<String, Object> params = new HashMap<>();

        params.put("userId", 2);
        params.put("comment", "Testing comment");

        Map<String, Double> locationParams = new HashMap<>();
        locationParams.put("longitude", -69.91605861112475);
        locationParams.put("latitude", 18.457826767442164);

        params.put("location", locationParams);

        List<Map<String, Object>> products = new ArrayList<>();
        Map<String, Object> product1 = new HashMap<>();
        product1.put("id", 13);
        product1.put("quantity", 4);

        products.add(product1);

        params.put("products", products);

        DeliveryppResponse<?> orderServiceResponse = orderService.createOrder(params);

        Assertions.assertTrue(orderServiceResponse.isSuccess(), "Failed creating order. " + orderServiceResponse.getMessage());

        params.remove("products");

        orderServiceResponse = orderService.createOrder(params);

        Assertions.assertTrue(!orderServiceResponse.isSuccess(), "Products are required.");

    }

    @Test
    public void get_order_by_id() {

        DeliveryppResponse<List<Order>> orderServiceResponse = orderService.getOrders();

        if(orderServiceResponse.isSuccess()) {
            List<Order> orders = orderServiceResponse.getResponse();
            if(orders.size() > 0) {
                Order order = orders.get(0);
                DeliveryppResponse<Order> getOrderByIdResponse = orderService.getOrderById(order.getId());
                Assertions.assertTrue(getOrderByIdResponse.isSuccess());
            }
        }
    }

    @Test
    public void update_order() {

    }

    @Test
    public void get_user_orders() {

    }




}
