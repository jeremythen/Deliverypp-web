package com.deliverypp.services.order;

import com.deliverypp.models.Order;
import com.deliverypp.models.User;
import com.deliverypp.util.DeliveryppResponse;

import java.util.List;
import java.util.Map;

public interface OrderService {

    DeliveryppResponse<List<Order>> getOrders();

    DeliveryppResponse<Order> getOrderById(int id);

    DeliveryppResponse<?> createOrder(Map<String, Object> requestMap);

    DeliveryppResponse<Order> updateStatus(int orderId, String status);

    DeliveryppResponse<Order> updateOrder(Order order);

    DeliveryppResponse<List<Order>> getUserOrders(User user);

}
