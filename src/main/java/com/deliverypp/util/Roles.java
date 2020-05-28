package com.deliverypp.util;

import com.deliverypp.models.User;

import java.util.Arrays;

import static java.util.Objects.*;

public enum Roles {

    USER("USER"), DELIVERY("DELIVERY"), ADMIN("ADMIN");

    private String role;

    Roles(String role) {
        this.role = role;
    }

    public static boolean isValidRole(String role) {

        if(isNull(role)) {
            return false;
        }

        return Arrays.stream(Roles.values())
                .anyMatch(roleObj -> roleObj.role.equals(role));

    }

    public static boolean isAdmin(User user) {
        return hasRole(user, ADMIN);
    }

    public static boolean isDelivery(User user) {
        return hasRole(user, DELIVERY);
    }

    public static boolean isRegularUser(User user) {
        return hasRole(user, USER);
    }

    private static boolean hasRole(User user, Roles role) {
        if(isNull(user)) {
            return false;
        }
        return role.name().equals(user.getRole());
    }

}