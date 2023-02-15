package org.social.model.user;

import lombok.Data;

@Data
public class UserVO {
    private String name;
    private String userName;
    private String password;
    private String email;
    private boolean isOnline;
}
