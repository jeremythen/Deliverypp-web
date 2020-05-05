package com.deliverypp.repositories;

import com.deliverypp.models.StripeCustomer;
import com.deliverypp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StripeCustomerRepository extends JpaRepository<StripeCustomer, Integer> {
    Optional<StripeCustomer> findByStripeCustomerId(String stripeCustomerId);
    Optional<StripeCustomer> findByUser(User user);
}
