package org.social.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.social.model.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {

    public User findByName(String name) {
        return find("name", name).firstResult();
    }

    public List<User> getAll() {
        return listAll();
    }
}
