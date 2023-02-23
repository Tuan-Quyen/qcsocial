package org.social.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.bson.types.ObjectId;
import org.social.model.chat.Chat;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ChatRepository implements PanacheMongoRepository<Chat> {

    public List<Chat> getAll(ObjectId roomId) {
        return find("roomId", roomId).list();
    }
}
