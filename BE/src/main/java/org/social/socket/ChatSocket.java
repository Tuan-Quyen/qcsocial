package org.social.socket;

import org.jboss.logging.Logger;
import org.social.model.SocketEvent;
import org.social.service.ChatService;
import org.social.service.RoomService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat/room/{roomId}/user/{userId}", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
@ApplicationScoped
public class ChatSocket {
    @Inject
    ChatService chatSvc;
    @Inject
    RoomService roomSvc;
    private static final Logger LOG = Logger.getLogger(ChatSocket.class);
    Map<String, Map<String, Session>> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId, @PathParam("userId") String userId) {
        LOG.infof("Open> : Room %s | User %s", roomId, userId);
        var userSession = Map.of(userId, session);
        sessions.computeIfAbsent(roomId, s -> new HashMap<>()).putAll(userSession);
        handleMessage(new SocketEvent(SocketEvent.Event.CONNECTED), roomId, userId);
    }

    @OnClose
    public void onClose(@PathParam("roomId") String roomId, @PathParam("userId") String userId) {
        LOG.infof("Close> : Room %s | User %s", roomId, userId);
        sessions.get(roomId).remove(userId);
        handleMessage(new SocketEvent(SocketEvent.Event.DISCONNECTED), roomId, userId);
    }

    @OnError
    public void onError(@PathParam("roomId") String roomId, @PathParam("userId") String userId, Throwable throwable) {
        LOG.infof("Error> : Room %s | User %s | Throw %s", roomId, userId, throwable);
        sessions.get(roomId).remove(userId);
        var socketEvent = new SocketEvent(SocketEvent.Event.ERROR);
        socketEvent.setData(throwable.getMessage());
        handleMessage(socketEvent, roomId, userId);
    }

    @OnMessage
    public void onMessage(SocketEvent socketEvent, @PathParam("roomId") String roomId, @PathParam("userId") String userId) {
        LOG.infof("Message> : Room %s | User %s", roomId, userId);
        LOG.infof("Message Data> :" + socketEvent);
        handleMessage(socketEvent, roomId, userId);
    }

    private void handleMessage(SocketEvent socketEvent, String roomId, String userId) {
        switch (socketEvent.getEvent()) {
            case SEND_MESSAGE -> {
                var chatData = chatSvc.saveMessage(socketEvent.getData());
                socketEvent.setData(chatData);
                broadcast(roomId, socketEvent);
            }
            case FETCH_MESSAGE -> {
                var chatData = chatSvc.findAll(roomId);
                socketEvent.setData(chatData);
                singleResponse(roomId, userId, socketEvent);
            }
            case CONNECTED -> {
                var roomData = roomSvc.insertUser(roomId, userId);
                socketEvent.setData(roomData);
                broadcast(roomId, socketEvent);
            }
            case DISCONNECTED -> {
                socketEvent.setData(userId);
                broadcast(roomId, socketEvent);
            }
            default -> singleResponse(roomId, userId, socketEvent);
        }
    }

    private void singleResponse(String roomId, String userId, SocketEvent socketEvent) {
        sessions.get(roomId).get(userId).getAsyncRemote().sendObject(socketEvent, result -> {
            if (result.getException() != null) {
                LOG.infof("Unable to send message: %s | %s", userId, result.getException());
            }
        });
    }

    private void broadcast(String roomId, SocketEvent socketEvent) {
        sessions.get(roomId).forEach((userId, session) -> session.getAsyncRemote().sendObject(socketEvent, result -> {
            if (result.getException() != null) {
                LOG.infof("Unable to send message: %s | %s", userId, result.getException());
            }
        }));
    }
}