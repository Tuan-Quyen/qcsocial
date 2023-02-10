package org.social.service;

import org.social.model.User;
import org.social.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepo;

    public List<User> findAll() {
        return userRepo.getAll();
    }

    public User register(User user) {
        var isCheckExist = userRepo.findByUser(user.getUserName());
        if (isCheckExist != null) {
            throw new RuntimeException(String.format("UserName '%s' already used.", user.getUserName()));
        }
        user.persistOrUpdate();
        return user;
    }
}
