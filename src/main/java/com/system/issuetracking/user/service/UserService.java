package com.system.issuetracking.user.service;


import com.system.issuetracking.entity.CoreService;
import com.system.issuetracking.user.model.User;

import java.util.List;

/**
 * @author Sunil Babu Shrestha on 3/18/2020
 */
public interface UserService extends CoreService<User> {
    User save(User event);

    List<User> findAll();

    void deleteById(long id);

    User findById(long id);

    User checkUserExist(User user);
}
