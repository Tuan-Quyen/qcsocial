package org.social.model.user;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@MongoEntity(collection="user")
public class User extends PanacheMongoEntity {
    private String name;
    private String userName;
    private String password;
    private String email;
    private String accessToken;
    private String refreshToken;
    private boolean isOnline;
}
