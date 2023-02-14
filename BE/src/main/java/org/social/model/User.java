package org.social.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private boolean isOnline;

    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
