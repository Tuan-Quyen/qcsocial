package org.social.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.bson.types.ObjectId;
import org.social.model.user.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {

    public User findByUser(String userName) {
        return find("userName", userName).firstResult();
    }

    public List<User> getAll() {
        return listAll();
    }

    public List<User> findByIds(List<ObjectId> ids) {
        return find("_id in ?1", ids).list();
    }
}
