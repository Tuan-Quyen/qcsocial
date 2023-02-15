package org.social.helper;

import org.social.model.room.Room;
import org.social.model.room.RoomVO;

public class RoomHelper {

    private RoomHelper() {
    }

    public static Room toEntity(RoomVO vo, Room entity) {
        if (entity == null) {
            entity = new Room();
        }
        entity.setName(vo.getName());
        entity.setUserHost(vo.getUserHost());
        return entity;
    }

    public static RoomVO toVO(Room entity) {
        var vo = new RoomVO();
        vo.setName(entity.getName());
        vo.setUserHost(entity.getUserHost());
        return vo;
    }
}
