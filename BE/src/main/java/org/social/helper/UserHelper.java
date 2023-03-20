package org.social.helper;

import org.social.model.user.User;
import org.social.model.user.UserVO;

public class UserHelper {
    private UserHelper() {
    }

    public static User toEntity(UserVO vo, User entity) {
        if (entity == null) {
            entity = new User();
        }
        entity.setName(vo.getName());
        entity.setEmail(vo.getEmail());
        entity.setUserName(vo.getUserName());
        entity.setPassword(vo.getPassword());
        return entity;
    }

    public static UserVO toVO(User entity) {
        var vo = new UserVO();
        vo.setId(entity.id.toString());
        vo.setName(entity.getName());
        vo.setEmail(entity.getEmail());
        vo.setUserName(entity.getUserName());
        return vo;
    }

    public static UserVO toVOIncludeToken(User entity) {
        var vo = new UserVO();
        vo.setId(entity.id.toString());
        vo.setName(entity.getName());
        vo.setEmail(entity.getEmail());
        vo.setUserName(entity.getUserName());
        vo.setAccessToken(entity.getAccessToken());
        vo.setRefreshToken(entity.getRefreshToken());
        return vo;
    }
}
