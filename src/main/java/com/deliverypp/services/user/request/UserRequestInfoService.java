package com.deliverypp.services.user.request;

import com.deliverypp.models.User;
import com.deliverypp.util.DeliveryppResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

public interface UserRequestInfoService {

    DeliveryppResponse<User> getUserFromRequest(HttpServletRequest request);

}
