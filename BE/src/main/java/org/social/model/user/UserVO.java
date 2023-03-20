package org.social.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO {
    private String id;
    private String name;
    private String userName;
    private String password;
    private String accessToken;
    private String refreshToken;
    private String email;
    private boolean online;
}
