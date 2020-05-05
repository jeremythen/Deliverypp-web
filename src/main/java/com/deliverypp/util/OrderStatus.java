package com.deliverypp.util;

import java.util.Arrays;

import static java.util.Objects.isNull;

public enum OrderStatus {

    ORDERED("ORDERED"), PAID("PAID"), ACQUIRING("ACQUIRING"), ACQUIRED("ACQUIRED"), TRANSIT("TRANSIT"), DELIVERED("DELIVERED");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public static boolean isValidStatus(String status) {

        if(isNull(status)) {
            return false;
        }

        return Arrays.stream(OrderStatus.values())
                .anyMatch(statusObj -> statusObj.status.equals(status));

    }

}
