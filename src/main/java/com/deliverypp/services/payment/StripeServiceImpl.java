package com.deliverypp.services.payment;

import com.deliverypp.models.DeliveryppParam;
import com.deliverypp.models.StripeCustomer;
import com.deliverypp.models.User;
import com.deliverypp.services.params.DeliveryppParamService;
import com.deliverypp.services.stripe.StripeCustomerService;
import com.deliverypp.services.user.UserService;
import com.deliverypp.util.DeliveryppResponse;
import static com.deliverypp.util.DeliveryppResponse.*;
import static com.deliverypp.util.DeliveryppResponseStatus.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StripeServiceImpl implements StripeService {

    private static final Logger logger = LoggerFactory.getLogger(StripeServiceImpl.class);

    @Value("${app.stripeApiTestKey}")
    private String stripeApiKey;

    private DeliveryppParamService deliveryppParamService;

    private StripeCustomerService stripeCustomerService;

    private UserService userService;

    @Autowired
    public StripeServiceImpl(DeliveryppParamService deliveryppParamService, StripeCustomerService stripeCustomerService,
                             UserService userService) {
        this.deliveryppParamService = deliveryppParamService;
        this.stripeCustomerService = stripeCustomerService;
        this.userService = userService;
    }

    @Override
    public DeliveryppResponse<?> createStripeCustomer(User user) {

        Stripe.apiKey = stripeApiKey;

        logger.info("createStripeUser user: {}", user);

        Map<String, Object> customerParameter = new HashMap<>();
        customerParameter.put("email", user.getEmail());

        logger.info("createStripeUser customerParameter: {}", customerParameter);

        try {
            Customer newCustomer = Customer.create(customerParameter);
            Map<String, Object> stripeCustomerResponse = new HashMap<>();

            String stripeCustomerId = newCustomer.getId();

            stripeCustomerResponse.put("stripeCustomerId", stripeCustomerId);

            StripeCustomer stripeCustomer = new StripeCustomer()
                                                    .setStripeCustomerId(stripeCustomerId)
                                                    .setUser(user);

            logger.info("createStripeUser stripeCustomer: {}", stripeCustomer);

            stripeCustomerService.addCustomer(stripeCustomer);

            return DeliveryppResponse.newResponse()
                    .setStatus(SUCCESS)
                    .setMessage("Successfully created Stripe customer.")
                    .setResponse(stripeCustomerResponse);
        } catch(StripeException e) {
            logger.error("Error creating stripe customer.", e);
            return DeliveryppResponse.newResponse()
                                        .setStatus(ERROR)
                                        .setMessage("Error creating Stripe customer.")
                                        .setSpecificStatus(STRIPE_USER_CREATE_FAIL)
                                        .setResponse(customerParameter);
        }
    }

    @Override
    public DeliveryppResponse<?> makePayment(Map<String, Object> requestMap) {
        Stripe.apiKey = stripeApiKey;

        String stripeCustomerId = (String) requestMap.get("stripeCustomerId");

        // TODO: Validate parameters

        try {

            Optional<Customer> optionalCustomer = getStripeCustomer(stripeCustomerId);

            if(optionalCustomer.isPresent()) {

                Customer customer = optionalCustomer.get();

                Map<String, Object> paymentParam = new HashMap<>();

                String amountString = (String) requestMap.get("amount");

                int amount = Integer.parseInt(amountString);

                DeliveryppResponse<?> paramResponse = deliveryppParamService.getParamsByKey("DOLLAR_IN_DOP");

                logger.info("makePayment paramResponse: {}", paramResponse);

                int dollarInDop = 5480;

                if(paramResponse.isSuccess()) {
                    DeliveryppParam deliveryppParam = (DeliveryppParam) paramResponse.getResponse();
                    String paramValue = deliveryppParam.getValue();
                    dollarInDop = Integer.parseInt(paramValue);
                }

                int convertedAmount = (amount * 10_000) / dollarInDop;

                paymentParam.put("amount", "" + convertedAmount);
                paymentParam.put("currency", "usd");
                paymentParam.put("customer", customer.getId());

                Charge.create(paymentParam);

                return DeliveryppResponse.newResponse()
                        .setStatus(SUCCESS)
                        .setMessage("Charge customer correctly.");
            } else {
                return DeliveryppResponse.newResponse()
                                            .setStatus(ERROR)
                                            .setMessage("Stripe customer not found.")
                                            .setSpecificStatus(STRIPE_USER_NOT_FOUND);
            }

        } catch(StripeException e) {
            logger.error("Error charging customer.", e);
            return DeliveryppResponse.newResponse()
                    .setStatus(ERROR)
                    .setMessage(e.getMessage());
        }

    }

    @Override
    public DeliveryppResponse<?> getCustomer(String email) {

        Stripe.apiKey = stripeApiKey;
        Map<String, Object> response = new HashMap<>();

        try {

            Customer customer = Customer.retrieve(email);
            response.put("email", customer.getEmail());
            response.put("id", customer.getId());
            response.put("createdAt", customer.getCreated());

            return DeliveryppResponse.newResponse()
                    .setStatus(SUCCESS)
                    .setMessage("Successfully retrieved Stripe customer.")
                    .setResponse(response);
        } catch (StripeException e) {
            logger.error("Error getting Stripe customer.", e);
            return DeliveryppResponse.newResponse()
                                        .setStatus(ERROR)
                                        .setMessage(e.getMessage());
        }

    }

    @Override
    public DeliveryppResponse<?> addCard(Map<String, Object> requestMap) {

        logger.info("addCard requestMap: {}", requestMap);

        Map<String, Object> cardParam = new HashMap<>();

        int deliveryppUserId = (Integer) requestMap.get("deliveryppUserId");

        DeliveryppResponse<User> userResponse = userService.findUserById(deliveryppUserId);

        if(!userResponse.isSuccess()) {
            return DeliveryppResponse.newResponse()
                    .setStatus(DeliveryppResponse.ERROR)
                    .setMessage("User not found.");
        }

        User user = userResponse.getResponse();
        DeliveryppResponse<?> stripeCustomerServiceResponse = stripeCustomerService.findByUser(user);

        if(!stripeCustomerServiceResponse.isSuccess()) {

            DeliveryppResponse<?> stripeCustomerResponse = createStripeCustomer(user);

            logger.info("addCard createStripeCustomer stripeCustomerResponse: {}", stripeCustomerResponse);

            if(stripeCustomerResponse.isSuccess()) {

                Map<String, Object> customerParameter = (Map<String, Object>) stripeCustomerResponse.getResponse();

                String stripeCustomerId = (String) customerParameter.get("stripeCustomerId");

                StripeCustomer stripeCustomer = new StripeCustomer()
                        .setUser(user)
                        .setStripeCustomerId(stripeCustomerId);
                stripeCustomerService.addCustomer(stripeCustomer);

                stripeCustomerServiceResponse = stripeCustomerService.findByUser(user);

            } else {
                return DeliveryppResponse.newResponse()
                        .setStatus(DeliveryppResponse.ERROR)
                        .setMessage("Stripe Customer not found.");
            }

        }

        StripeCustomer stripeCustomer = (StripeCustomer) stripeCustomerServiceResponse.getResponse();

        String stripeUserId = stripeCustomer.getStripeCustomerId();

        cardParam.put("number", requestMap.get("number"));
        cardParam.put("exp_month", requestMap.get("exp_month"));
        cardParam.put("exp_year", requestMap.get("exp_year"));
        cardParam.put("cvc", requestMap.get("123"));

        try {

            Customer customer = Customer.retrieve(stripeUserId);

            Map<String, Object> tokenParam = new HashMap<>();
            tokenParam.put("card", cardParam);

            Token token = Token.create(tokenParam);

            Map<String, Object> source = new HashMap<>();

            source.put("source", token.getId());

            customer.getSources().create(source);

            return DeliveryppResponse.newResponse()
                    .setStatus(DeliveryppResponse.SUCCESS)
                    .setMessage("Successfully added user card");

        } catch (StripeException e) {
            logger.error("Error adding customer Stripe card.", e);
            return DeliveryppResponse.newResponse()
                                        .setStatus(DeliveryppResponse.ERROR)
                                        .setMessage(e.getMessage());
        }

    }

    private Optional<Customer> getStripeCustomer(String stripeCustomerId) {
        logger.info("getStripeCustomer stripeCustomerId: {}", stripeCustomerId);
        try {
            Customer customer = Customer.retrieve(stripeCustomerId);
            return Optional.of(customer);
        } catch(StripeException e) {
            logger.error("Error retrieving customer.", e);
        }
        return Optional.empty();
    }

}
