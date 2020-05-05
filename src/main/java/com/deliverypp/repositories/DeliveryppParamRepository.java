package com.deliverypp.repositories;

import com.deliverypp.models.DeliveryppParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryppParamRepository  extends JpaRepository<DeliveryppParam, Integer> {

    Optional<DeliveryppParam> findByKey(String key);

}
