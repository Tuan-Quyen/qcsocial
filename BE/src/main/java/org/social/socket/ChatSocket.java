package org.social.socket;

import org.jboss.logging.Logger;
import org.social.model.SessionClient;
import org.social.model.SocketEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat/room/{roomId}/user/{userId}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
@ApplicationScoped
public class ChatSocket {
    private static final Logger LOG = Logger.getLogger(ChatSocket.class);
    Map<String, List<SessionClient>> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId, @PathParam("userId") String userId) {
        LOG.infof("Open> : Room %s | User %s", roomId, userId);
        var sessionClient = new SessionClient();
        sessionClient.setUserId(userId);
        sessionClient.setSession(session);
        sessions.computeIfAbsent(userId, s -> new ArrayList<>()).add(sessionClient);
    }

    @OnClose
    public void onClose(@PathParam("roomId") String roomId, @PathParam("userId") String userId) {
        LOG.infof("Close> : Room %s | User %s", roomId, userId);
        sessions.remove(userId);
        var data = "User " + userId + " left";
        var socketEvent = new SocketEvent(SocketEvent.Event.DISCONNECTED, data);
        broadcast(socketEvent);
    }

    @OnError
    public void onError(@PathParam("roomId") String roomId, @PathParam("userId") String userId, Throwable throwable) {
        LOG.infof("Error> : Room %s | User %s", roomId, userId);
        sessions.remove(userId);
        var data = "User " + userId + " left on error: " + throwable;
        var socketEvent = new SocketEvent(SocketEvent.Event.DISCONNECTED, data);
        broadcast(socketEvent);
    }

    @OnMessage
    public void onMessage(SocketEvent receiveData, @PathParam("roomId") String roomId, @PathParam("userId") String userId) {
        LOG.infof("Message> : Room %s | User %s", roomId, userId);
        LOG.infof("Message Data> :" + receiveData);
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
        /*sessions.values().forEach(s -> s.getAsyncRemote().sendObject(socketEvent, result -> {
            if (result.getException() != null) {
                LOG.infof("Unable to send message: " + result.getException());
            }
        }));*/
    }
}