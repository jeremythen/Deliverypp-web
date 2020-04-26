package com.deliverypp.services.user;
import com.deliverypp.models.User;

import com.deliverypp.util.DeliveryppResponse;

public interface UserService {

    Iterable<User> getUsers();

    DeliveryppResponse<User> save(User user);

    User findByUsername(String username);

    DeliveryppResponse<User> findUserById(int id);

}
