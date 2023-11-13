package com.quiroz.mypayments.exception.dto;

public enum ErrorMessage {
    INTERNAL_SERVER_ERROR(Keys.INTERNAL_SERVER_ERROR),
    RESOURCE_NOT_FOUND(Keys.RESOURCE_NOT_FOUND),
    SPECIFIED_RESOURCE_NOT_FOUND(Keys.SPECIFIED_RESOURCE_NOT_FOUND),
    BAD_REQUEST(Keys.BAD_REQUEST);

    private static final class Keys {
        private static final String INTERNAL_SERVER_ERROR = "internal.server.error";
        private static final String RESOURCE_NOT_FOUND = "resource.not.found";
        private static final String BAD_REQUEST = "bad.request";
        private static final String SPECIFIED_RESOURCE_NOT_FOUND = ".not.found";

        private Keys() {

        }
    };

    private final String key;

    ErrorMessage(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
