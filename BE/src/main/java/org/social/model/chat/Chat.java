package org.social.model.chat;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@MongoEntity(collection="chat")
public class Chat extends PanacheMongoEntity {
    private String message;
    private ObjectId userId;
    private ObjectId roomId;
}
