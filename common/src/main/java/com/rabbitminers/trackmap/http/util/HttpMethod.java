package com.rabbitminers.trackmap.http.util;

import java.util.Locale;

public enum HttpMethod {
    GET, POST, PUT, DELETE;

    public static HttpMethod fromString(String method) {
        try {
            return valueOf(method.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
