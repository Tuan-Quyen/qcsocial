package org.social.model.response;

import lombok.Data;

@Data
public class HttpResult {
    private int statusCode;
    private String message;
}
