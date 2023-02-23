package org.social.service;

import org.bson.types.ObjectId;
import org.social.helper.RoomHelper;
import org.social.helper.UserHelper;
import org.social.model.room.Room;
import org.social.model.exception.ConflictException;
import org.social.model.exception.NotFoundException;
import org.social.model.room.RoomVO;
import org.social.repository.RoomRepository;
import org.social.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class RoomService {
    @Inject
    UserService userService;
    @Inject
    RoomRepository roomRepo;
    @Inject
    UserRepository userRepo;

    public List<RoomVO> findAll() {
        return roomRepo.getAll().stream().map(entity -> {
            var vo = RoomHelper.toVO(entity);
            injectUser(entity.getExistUser(), vo);
            return vo;
        }).toList();
    }

    public Room findByName(String name) {
        var room = roomRepo.findByName(name);
        if (room == null) {
            throw new NotFoundException(String.format("Room '%s' is not found.", name));
        }
        return room;
    }

    public Room findById(String id) {
        var objectId = new ObjectId(id);
        var room = roomRepo.findById(objectId);
        if (room == null) {
            throw new NotFoundException(String.format("Room id '%s' is not found.", id));
        }
        return room;
    }

    public Room create(RoomVO vo) {
        var entity = roomRepo.findByName(vo.getName());
        if (entity != null) {
            throw new ConflictException(String.format("Room name '%s' already used.", vo.getName()));
        }
        var user = userService.findByUser(vo.getUserHost());
        entity = RoomHelper.toEntity(vo, null);
        entity.setExistUser(Collections.singletonList(user.id));
        entity.persistOrUpdate();
        return entity;
    }

    public Room update(RoomVO room) {
        var entity = findByName(room.getName());
        RoomHelper.toEntity(room, entity);
        entity.persistOrUpdate();
        return entity;
    }

    public RoomVO insertUser(String roomId, String userId) {
        var entity = findById(roomId);
        var user = userService.findById(userId);
        boolean isExisted = entity.getExistUser().contains(user.id);
        if (!isExisted) {
            entity.getExistUser().add(user.id);
        }
        entity.persistOrUpdate();
        var vo = RoomHelper.toVO(entity);
        injectUser(entity.getExistUser(), vo);
        return vo;
    }

    public void deleteByName(String name) {
        var entity = findByName(name);
        roomRepo.delete(entity);
    }

    public void deleteById(String id) {
        var entity = findById(id);
        roomRepo.delete(entity);
    }

    public void injectUser(List<ObjectId> userId, RoomVO vo) {
        var users = userRepo.findByIds(userId).stream().map(UserHelper::toVO).toList();
        vo.setExistUser(users);
    }
}
