/*package org.ptxm;

import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint("/chat/{roomId}")
@ApplicationScoped
public class SocketServer {
    private static final Logger LOG = Logger.getLogger(SocketServer.class);
    private final Map<Long, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") long roomId,
                       @PathParam("user") String user) {
        LOG.infof("onOpen> " + name);
        sessions.put(roomId, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("name") String name) {
        LOG.infof("onClose> " + name);
        sessions.remove(username);
    }

    @OnError
    public void onError(Session session, @PathParam("name") String name, Throwable throwable) {
        LOG.errorf("onError> " + name + ": " + throwable);
        sessions.remove(username);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("name") String name) {
        LOG.infof("onMessage> " + name + ": " + message);
        if (message.equalsIgnoreCase("_ready_")) {
            putMessage("User " + username + " joined");
        } else {
            putMessage(">> " + username + ": " + message);
        }
    }

    private void putMessage(String roomId, String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendText(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }

}*/
