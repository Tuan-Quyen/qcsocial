package org.social.model;

import lombok.Data;

@Data
public class SocketEvent {
    private Event event;
    private Object data;

    public SocketEvent() {
    }

    public SocketEvent(Event event, Object data) {
        this.event = event;
        this.data = data;
    }

    public enum Event {
        CONNECTED,
        JOIN_ROOM,
        OUT_ROOM,
        DISCONNECTED,
        SEND_MESSAGE,
        DELETE_MESSAGE
    }
}
