package com.deliverypp.services.payment;

import com.deliverypp.models.Order;
import com.deliverypp.models.User;
import com.deliverypp.util.DeliveryppResponse;

import java.util.Map;

public interface StripeService {

    DeliveryppResponse<?> createStripeCustomer(User user);
    DeliveryppResponse<?> makePayment(Map<String, Object> options);
    DeliveryppResponse<?> getCustomer(String email);
    DeliveryppResponse<?> addCard(Map<String, Object> requestMap);

}
