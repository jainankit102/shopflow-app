package com.shopflow.common.web;
import org.springframework.http.HttpStatus;


public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String code;
    // private final String errorMessage;

    public ApiException(HttpStatus status, String code, String errorMessage) {
        super(errorMessage);
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public static ApiException notFound(String what, String id) {
        return new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", what + " with ID " + id + " not found");
    }

    public static ApiException badRequest(String code, String reason) {
        return new ApiException(HttpStatus.BAD_REQUEST, code, "Invalid request: " + reason);
    }

    public static ApiException internalServerError(String code, String reason) {
        return new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, code, "Internal server error: " + reason);
    }

    public static ApiException conflict(String code, String message) {
        return new ApiException(HttpStatus.CONFLICT, code, message);
    }

    public static ApiException unauthorized(String code, String message) {
        return new ApiException(HttpStatus.UNAUTHORIZED, code, message);
    }

    public static ApiException forbidden(String code, String message) {
        return new ApiException(HttpStatus.FORBIDDEN, code, message);
    }

    public static ApiException unavailable(String code, String message) {
        return new ApiException(HttpStatus.SERVICE_UNAVAILABLE, code, message);
    }

}