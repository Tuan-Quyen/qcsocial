package org.social.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.social.model.User;

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
}
