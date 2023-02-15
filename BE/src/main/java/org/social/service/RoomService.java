package org.social.service;

import org.bson.types.ObjectId;
import org.social.model.room.Room;
import org.social.model.exception.ConflictException;
import org.social.model.exception.NotFoundException;
import org.social.repository.RoomRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class RoomService {
    @Inject
    RoomRepository roomRepo;

    public List<Room> findAll() {
        return roomRepo.getAll();
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

    public Room register(Room room) {
        var entity = roomRepo.findByName(room.getName());
        if (entity != null) {
            throw new ConflictException(String.format("Room name '%s' already used.", room.getName()));
        }
        roomRepo.persistOrUpdate(room);
        return room;
    }

    public Room update(Room room) {
        findByName(room.getName());
        roomRepo.update(room);
        return room;
    }

    public void deleteByName(String name) {
        var entity = findByName(name);
        roomRepo.delete(entity);
    }

    public void deleteById(String id) {
        var entity = findById(id);
        roomRepo.delete(entity);
    }
}
