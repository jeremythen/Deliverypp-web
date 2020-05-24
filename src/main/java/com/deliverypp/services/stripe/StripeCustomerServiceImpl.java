package com.deliverypp.services.stripe;

import com.deliverypp.models.StripeCustomer;
import com.deliverypp.models.User;
import com.deliverypp.repositories.StripeCustomerRepository;
import com.deliverypp.util.DeliveryppLoggin;
import com.deliverypp.util.DeliveryppResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@DeliveryppLoggin
public class StripeCustomerServiceImpl implements StripeCustomerService {

    private StripeCustomerRepository stripeCustomerRepository;

    @Autowired
    public StripeCustomerServiceImpl(StripeCustomerRepository stripeCustomerRepository) {
        this.stripeCustomerRepository = stripeCustomerRepository;
    }

    @Override
    public DeliveryppResponse<?> addCustomer(StripeCustomer stripeCustomer) {

        stripeCustomerRepository.save(stripeCustomer);
        return DeliveryppResponse.newResponse()
                .setStatus(DeliveryppResponse.SUCCESS)
                .setMessage("Added Stripe Customer.")
                .setResponse(stripeCustomer);

    }

    @Override
    public DeliveryppResponse<?> getStripeCustomers() {

        List<StripeCustomer> stripeCustomers = stripeCustomerRepository.findAll();

        return DeliveryppResponse.newResponse()
                .setStatus(DeliveryppResponse.SUCCESS)
                .setMessage("Retrieved Stripe Customers.")
                .setResponse(stripeCustomers);
    }

    @Override
    public DeliveryppResponse<?> getById(int id) {

        Optional<StripeCustomer> optionalStripeCustomer = stripeCustomerRepository.findById(id);

        if(optionalStripeCustomer.isPresent()) {
            return DeliveryppResponse.newResponse()
                    .setStatus(DeliveryppResponse.SUCCESS)
                    .setMessage("Retrieved Stripe Customer by id.")
                    .setResponse(optionalStripeCustomer.get());
        } else {
            return DeliveryppResponse.newResponse()
                    .setStatus(DeliveryppResponse.ERROR)
                    .setMessage("Error retrieving Stripe Customer by id.");
        }
    }

    @Override
    public DeliveryppResponse<?> getByStripeCustomerId(String stripeCustomerId) {

        Optional<StripeCustomer> optionalStripeCustomer = stripeCustomerRepository.findByStripeCustomerId(stripeCustomerId);;

        if(optionalStripeCustomer.isPresent()) {
            return DeliveryppResponse.newResponse()
                    .setStatus(DeliveryppResponse.SUCCESS)
                    .setMessage("Retrieved Stripe Customer by stripeCustomerId.")
                    .setResponse(optionalStripeCustomer.get());
        } else {
            return DeliveryppResponse.newResponse()
                    .setStatus(DeliveryppResponse.ERROR)
                    .setMessage("Error retrieving Stripe Customer by stripeCustomerId.");
        }

    }

    @Override
    public DeliveryppResponse<?> findByUser(User user) {
        Optional<StripeCustomer> optionalStripeCustomer = stripeCustomerRepository.findByUser(user);

        if(optionalStripeCustomer.isPresent()) {
            return DeliveryppResponse.newResponse()
                    .setStatus(DeliveryppResponse.SUCCESS)
                    .setMessage("Retrieved Stripe Customer by user.")
                    .setResponse(optionalStripeCustomer.get());
        } else {
            return DeliveryppResponse.newResponse()
                    .setStatus(DeliveryppResponse.ERROR)
                    .setMessage("Error retrieving Stripe Customer by user.");
        }

    }


}
