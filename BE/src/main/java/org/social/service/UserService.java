package org.social.service;

import io.quarkus.security.UnauthorizedException;
import org.bson.types.ObjectId;
import org.social.helper.UserHelper;
import org.social.model.user.User;
import org.social.model.exception.ConflictException;
import org.social.model.exception.NotFoundException;
import org.social.model.user.UserVO;
import org.social.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@ApplicationScoped
public class UserService implements UserInterface{
    @Inject
    TokenUtils tokenUtils;
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

    public User register(UserVO user) {
        User entity = userRepo.findByUser(user.getUserName());
        if (entity != null) {
            throw new ConflictException(String.format("UserName '%s' already used.", user.getUserName()));
        }
        entity = UserHelper.toEntity(user, null);
        entity.persistOrUpdate();
        return entity;
    }

    public User update(UserVO user) {
        User entity = findByUser(user.getUserName());
        entity = UserHelper.toEntity(user, entity);
        entity.persistOrUpdate();
        return entity;
    }

    public void deleteByName(String userName) {
        var entity = findByUser(userName);
        userRepo.delete(entity);
    }

    public void deleteById(String id) {
        var entity = findById(id);
        userRepo.delete(entity);
    }

    public User handleLogin(String userName, String password) {
        var user = findByUser(userName);
        if (!user.getPassword().equals(password)) {
            throw new UnauthorizedException("Password is incorrect.");
        }
        user.setAccessToken(tokenUtils.generateToken(user.id.toString()));
        user.setRefreshToken(tokenUtils.generateRefreshToken(user.id.toString()));
        user.setOnline(true);
        user.persistOrUpdate();
        return user;
    }


    public String refreshToken(SecurityContext ctx) {
        return tokenUtils.refreshToken(ctx, (user, token) -> {
            user.setAccessToken(token);
            user.persistOrUpdate();
        });
    }
    public void handleLogout(SecurityContext ctx) {
        tokenUtils.clearToken(ctx, user -> {
            user.setAccessToken(null);
            user.setOnline(true);
            user.persistOrUpdate();
        });
    }

    @Override
    public User findUserById(String id) {
        return findById(id);
    }
}