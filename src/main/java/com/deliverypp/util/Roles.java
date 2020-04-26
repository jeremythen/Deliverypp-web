package com.deliverypp.util;

import java.util.Arrays;

import static java.util.Objects.*;

public enum Roles {

    USER("USER"), DELIVERY("USER"), ADMIN("USER");

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

}