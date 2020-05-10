package com.deliverypp.repositories;

import com.deliverypp.models.Order;
import com.deliverypp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findAllByUser(User user);

}
