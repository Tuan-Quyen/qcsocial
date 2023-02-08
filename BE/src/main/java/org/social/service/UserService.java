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
}
