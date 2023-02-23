package org.social.model;

import lombok.Data;

@Data
public class SocketEvent {
    private Event event;
    private Object data;

    public SocketEvent() {
    }

    public SocketEvent(Event event) {
        this.event = event;
    }

    public enum Event {
        CONNECTED,
        JOIN_ROOM,
        ERROR,
        FETCH_MESSAGE,
        DISCONNECTED,
        SEND_MESSAGE,
        DELETE_MESSAGE
    }
}
