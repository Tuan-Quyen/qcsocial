package org.social.model.room;

import lombok.Data;
import org.social.model.user.UserVO;

import java.util.List;

@Data
public class RoomVO {
    private String name;
    private String userHost;
    private List<UserVO> existUser;
}
