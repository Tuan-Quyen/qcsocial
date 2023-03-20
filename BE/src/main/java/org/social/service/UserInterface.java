package org.social.service;

import org.social.model.user.User;

public interface UserInterface {
    User findUserById(String id);
}
