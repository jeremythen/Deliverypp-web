package com.deliverypp.services.params;

import com.deliverypp.models.DeliveryppParam;
import com.deliverypp.repositories.DeliveryppParamRepository;
import com.deliverypp.util.DeliveryppResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryppParamServiceImpl implements DeliveryppParamService {

    private DeliveryppParamRepository deliveryppParamRepository;

    @Autowired
    public DeliveryppParamServiceImpl(DeliveryppParamRepository deliveryppParamRepository) {
        this.deliveryppParamRepository = deliveryppParamRepository;
    }

    @Override
    public DeliveryppResponse<?> addParam(DeliveryppParam deliveryppParam) {

        Optional<DeliveryppParam> paramResponse = deliveryppParamRepository.findByKey(deliveryppParam.getKey());

        if(paramResponse.isPresent()) {
            return DeliveryppResponse.newResponse()
                    .setStatus(DeliveryppResponse.ERROR)
                    .setMessage("Param exists.");
        }

        deliveryppParamRepository.save(deliveryppParam);
        return DeliveryppResponse.newResponse()
                                    .setStatus(DeliveryppResponse.SUCCESS)
                                    .setMessage("Added param.")
                                    .setResponse(deliveryppParam);
    }

    @Override
    public DeliveryppResponse<?> updateParam(DeliveryppParam deliveryppParam) {

        deliveryppParam.setUpdatedAt(LocalDateTime.now());
        deliveryppParamRepository.save(deliveryppParam);

        return DeliveryppResponse.newResponse()
                .setStatus(DeliveryppResponse.SUCCESS)
                .setMessage("Deleted param.")
                .setResponse(deliveryppParam);
    }

    @Override
    public DeliveryppResponse<?> deleteParamById(int paramId) {

        deliveryppParamRepository.deleteById(paramId);

        return DeliveryppResponse.newResponse()
                .setStatus(DeliveryppResponse.SUCCESS)
                .setMessage("Deleted param.");
    }

    @Override
    public DeliveryppResponse<?> getParams() {

        List<DeliveryppParam> params = deliveryppParamRepository.findAll();

        return DeliveryppResponse.newResponse()
                .setStatus(DeliveryppResponse.SUCCESS)
                .setMessage("Deleted param.")
                .setResponse(params);

    }

    @Override
    public DeliveryppResponse<?> getParamsByKey(String key) {

        Optional<DeliveryppParam> optionalParam = deliveryppParamRepository.findByKey(key);

        if(optionalParam.isPresent()) {
            DeliveryppParam param = optionalParam.get();
            return DeliveryppResponse.newResponse()
                    .setStatus(DeliveryppResponse.SUCCESS)
                    .setMessage("Successfully retrieved param by key.")
                    .setResponse(param);
        } else {
            return DeliveryppResponse.newResponse()
                    .setStatus(DeliveryppResponse.ERROR)
                    .setMessage("Error retrieving param by key.");
        }

    }

    @Override
    public DeliveryppResponse<?> getParamsById(int id) {

        Optional<DeliveryppParam> optionalParam = deliveryppParamRepository.findById(id);

        if(optionalParam.isPresent()) {
            DeliveryppParam param = optionalParam.get();
            return DeliveryppResponse.newResponse()
                    .setStatus(DeliveryppResponse.SUCCESS)
                    .setMessage("Successfully retrieved param by id.")
                    .setResponse(param);
        } else {
            return DeliveryppResponse.newResponse()
                    .setStatus(DeliveryppResponse.ERROR)
                    .setMessage("Error retrieving param by id.");
        }

    }
}
