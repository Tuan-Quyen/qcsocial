package org.social.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.social.model.chat.Chat;
import org.social.model.chat.ChatVO;
import org.social.repository.ChatRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ChatService {
    @Inject
    ChatRepository chatRepo;
    @Inject
    UserService userService;
    @Inject
    ObjectMapper objectMapper;
    public List<ChatVO> findAll(String roomId) {
        return chatRepo.getAll(new ObjectId(roomId)).stream().map(this::convertVO).toList();
    }

    @SneakyThrows
    public ChatVO saveMessage(Object data) {
        var receiveData = objectMapper.convertValue(data, ChatReceive.class);
        var entity = new Chat();
        entity.setMessage(receiveData.getMessage());
        entity.setUserId(new ObjectId(receiveData.getUserId()));
        entity.setRoomId(new ObjectId(receiveData.getRoomId()));
        entity.persistOrUpdate();
        return convertVO(entity);
    }

    private ChatVO convertVO(Chat entity) {
        var vo = new ChatVO();
        vo.setMessage(entity.getMessage());
        vo.setRoomId(entity.getRoomId().toString());
        var user = userService.findById(entity.getUserId().toString());
        var userInfoMap = new HashMap<String, Object>();
        userInfoMap.put("userId", user.id.toString());
        userInfoMap.put("name", user.getName());
        userInfoMap.put("email", user.getEmail());
        vo.setUser(userInfoMap);
        return vo;
    }


}

@Data
class ChatReceive {
    private String message;
    private String userId;
    private String roomId;
}
