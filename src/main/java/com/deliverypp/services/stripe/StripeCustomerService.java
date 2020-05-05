package com.deliverypp.services.stripe;

import com.deliverypp.models.StripeCustomer;
import com.deliverypp.models.User;
import com.deliverypp.util.DeliveryppResponse;

public interface StripeCustomerService {

    DeliveryppResponse<?> addCustomer(StripeCustomer stripeCustomer);
    DeliveryppResponse<?> getStripeCustomers();
    DeliveryppResponse<?> getById(int id);
    DeliveryppResponse<?> getByStripeCustomerId(String stripeCustomerId);
    DeliveryppResponse<?> findByUser(User user);

}
