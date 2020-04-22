package com.deliverypp.util;

import org.springframework.http.ResponseEntity;

public class DeliveryppResponse<T> {

    public static final String ERROR = "ERROR";
    public static final String SUCCESS = "SUCCESS";

    public DeliveryppResponse() {}

    public DeliveryppResponse(String status, String message, T response) {
        this.status = status;
        this.message = message;
        this.response = response;
    }

    private String status;

    private String message;

    private T response;

    public String getStatus() {
        return status;
    }

    public DeliveryppResponse<T> setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public DeliveryppResponse<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getResponse() {
        return response;
    }

    public DeliveryppResponse<T> setResponse(T response) {
        this.response = response;
        return this;
    }

    public static ResponseEntity<DeliveryppResponse<?>> getDefaultResponse(DeliveryppResponse<?> response) {
        if(ERROR.equals(response.getStatus())) {
            return ResponseEntity.badRequest().body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }

}
