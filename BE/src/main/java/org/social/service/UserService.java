package org.social.service;

import io.quarkus.security.UnauthorizedException;
import org.bson.types.ObjectId;
import org.social.model.user.User;
import org.social.model.exception.ConflictException;
import org.social.model.exception.NotFoundException;
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

    public User findByUser(String userName) {
        var user = userRepo.findByUser(userName);
        if (user == null) {
            throw new NotFoundException(String.format("User '%s' is not found.", userName));
        }
        return user;
    }

    public User findById(String id) {
        var objectId = new ObjectId(id);
        var user = userRepo.findById(objectId);
        if (user == null) {
            throw new NotFoundException(String.format("User id '%s' is not found.", id));
        }
        return user;
    }

    public User register(User user) {
        var entity = userRepo.findByUser(user.getUserName());
        if (entity != null) {
            throw new ConflictException(String.format("UserName '%s' already used.", user.getUserName()));
        }
        userRepo.persistOrUpdate(user);
        return user;
    }

    public User update(User user) {
        findByUser(user.getUserName());
        userRepo.update(user);
        return user;
    }

    public void deleteByName(String userName) {
        var entity = findByUser(userName);
        userRepo.delete(entity);
    }

    public void deleteById(String id) {
        var entity = findById(id);
        userRepo.delete(entity);
    }

    public void handleLogin(String userName, String password) {
        var isSuccess = userRepo.login(userName, password);
        if (!isSuccess) {
            throw new UnauthorizedException("UserName or password is incorrect.");
        }
    }
}
