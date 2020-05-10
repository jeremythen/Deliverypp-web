package com.deliverypp.services.user.request;

import com.deliverypp.models.User;
import com.deliverypp.security.JwtTokenProvider;
import com.deliverypp.services.user.UserService;
import com.deliverypp.util.DeliveryppResponse;
import com.deliverypp.util.DeliveryppResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserRequestInfoServiceImpl implements UserRequestInfoService {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Override
    public DeliveryppResponse<User> getUserFromRequest(HttpServletRequest request) {

        String jwt = getJwtFromRequest(request);

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            int userId = tokenProvider.getUserIdFromJWT(jwt);

            DeliveryppResponse<User> userServiceResponse = userService.findUserById(userId);

            if(!userServiceResponse.isSuccess()) {
                return new DeliveryppResponse<User>()
                        .setStatus(DeliveryppResponse.ERROR)
                        .setSpecificStatus(DeliveryppResponseStatus.USER_NOT_FOUND);
            }

            User user = userServiceResponse.getResponse();

            return new DeliveryppResponse<User>()
                    .setStatus(DeliveryppResponse.SUCCESS)
                    .setResponse(user);

        }

        return new DeliveryppResponse<User>()
                .setStatus(DeliveryppResponse.ERROR)
                .setSpecificStatus(DeliveryppResponseStatus.TOKEN_NOT_VALID);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}
