package org.social.model;

import lombok.Data;

import javax.websocket.Session;

@Data
public class SessionClient {
    private String userId;
    private Session session;
}
