package com.deliverypp.repositories;

import com.deliverypp.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByVisible(boolean visible);

    Optional<Product> findByIdAndVisible(int id, boolean visible);

}
