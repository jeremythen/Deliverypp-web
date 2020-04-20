package com.deliverypp.repositories;

import com.deliverypp.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<Address, Integer> {
}
