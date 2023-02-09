package org.social.socket;

import org.jboss.logging.Logger;
import org.social.model.SocketEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat/{userId}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
@ApplicationScoped
public class ChatSocket {
    private static final Logger LOG = Logger.getLogger(ChatSocket.class);
    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        LOG.infof("Open> :" + userId);
        sessions.put(userId, session);
        var data = "User " + userId + " connected";
        var socketEvent = new SocketEvent(SocketEvent.Event.CONNECTED, data);
        broadcast(socketEvent);
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") String userId) {
        LOG.infof("Close> :" + userId);
        sessions.remove(userId);
        var data = "User " + userId + " left";
        var socketEvent = new SocketEvent(SocketEvent.Event.DISCONNECTED, data);
        broadcast(socketEvent);
    }

    @OnError
    public void onError(Session session, @PathParam("userId") String userId, Throwable throwable) {
        LOG.infof("Error> :" + userId);
        sessions.remove(userId);
        var data = "User " + userId + " left on error: " + throwable;
        var socketEvent = new SocketEvent(SocketEvent.Event.DISCONNECTED, data);
        broadcast(socketEvent);
    }

    @OnMessage
    public void onMessage(SocketEvent receiveData, @PathParam("userId") String userId) {
        LOG.infof("Message> :" + receiveData);
        var sendData = new SocketEvent();
        switch (receiveData.getEvent()) {
            case SEND_MESSAGE -> {
                sendData.setEvent(SocketEvent.Event.SEND_MESSAGE);
                sendData.setData(receiveData.getData());
                break;
            }
        }
        broadcast(sendData);
    }

    private void broadcast(SocketEvent socketEvent) {
        sessions.values().forEach(s -> s.getAsyncRemote().sendObject(socketEvent, result -> {
            if (result.getException() != null) {
                LOG.infof("Unable to send message: " + result.getException());
            }
        }));
    }
}