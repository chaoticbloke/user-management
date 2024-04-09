package com.users.management.domain;

import org.springframework.http.HttpStatus;

public class HttpResponse {
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;
}
