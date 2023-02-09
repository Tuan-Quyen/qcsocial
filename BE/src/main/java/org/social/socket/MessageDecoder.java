package org.social.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.social.model.SocketEvent;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<SocketEvent> {
    private ObjectMapper objectMapper;

    @Override
    public SocketEvent decode(String data) throws DecodeException {
        try {
            return objectMapper.readValue(data, SocketEvent.class);
        } catch (JsonProcessingException e) {
            throw new DecodeException(data, "Fail to decode.", e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void destroy() {
        objectMapper = null;
    }
}
