package org.social.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.social.model.room.Room;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class RoomRepository implements PanacheMongoRepository<Room> {
    public Room findByName(String name) {
        return find("name", name).firstResult();
    }
    public List<Room> getAll() {
        return listAll();
    }
}
