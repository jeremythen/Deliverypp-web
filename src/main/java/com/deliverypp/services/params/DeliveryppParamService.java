package com.deliverypp.services.params;

import com.deliverypp.models.DeliveryppParam;
import com.deliverypp.util.DeliveryppResponse;

public interface DeliveryppParamService {

    DeliveryppResponse<?> addParam(DeliveryppParam deliveryppParam);
    DeliveryppResponse<?> updateParam(DeliveryppParam deliveryppParam);
    DeliveryppResponse<?> deleteParamById(int paramId);
    DeliveryppResponse<?> getParams();
    DeliveryppResponse<?> getParamsByKey(String key);
    DeliveryppResponse<?> getParamsById(int id);

}
