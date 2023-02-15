package org.social.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.social.model.user.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {

    public User findByUser(String userName) {
        return find("userName", userName).firstResult();
    }

    public boolean login(String userName, String password) {
        return find("userName = ?1 & passWord = ?2", userName, password).firstResult() != null;
    }

    public List<User> getAll() {
        return listAll();
    }

    public List<User> findByIds(List<String> ids) {
        return find("id in ?1", ids).list();
    }
}
