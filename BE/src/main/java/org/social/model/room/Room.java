package org.social.model.room;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@MongoEntity(collection="room")
public class Room extends PanacheMongoEntity {
    private String name;
    private String userHost;
    private List<String> existUser;
}
