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

    private DeliveryppResponseStatus specificStatus;

    private T response;

    private boolean isSuccess;

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

    public DeliveryppResponseStatus getSpecificStatus() {
        return specificStatus;
    }

    public DeliveryppResponse<T> setSpecificStatus(DeliveryppResponseStatus specificStatus) {
        this.specificStatus = specificStatus;
        return this;
    }

    public static ResponseEntity<DeliveryppResponse<?>> getDefaultResponse(DeliveryppResponse<?> response) {
        if(ERROR.equals(response.getStatus())) {
            return ResponseEntity.badRequest().body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    public static <T> DeliveryppResponse<T> newResponse() {
        return new DeliveryppResponse<>();
    }

    public boolean isSuccess() {
        return isSuccess || SUCCESS.equals(status);
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @Override
    public String toString() {
        return "DeliveryppResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", specificStatus=" + specificStatus +
                ", response=" + response +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
