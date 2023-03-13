package org.social.model.room;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.social.model.user.UserVO;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomVO {
    private String id;
    private String name;
    private String userHost;
    private List<UserVO> existUser;
}
