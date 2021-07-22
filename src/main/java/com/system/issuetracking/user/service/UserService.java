package com.system.issuetracking.user.service;


import com.system.issuetracking.entity.CoreService;
import com.system.issuetracking.user.model.Role;
import com.system.issuetracking.user.model.User;

import javax.mail.MessagingException;
import java.util.List;

/**
 * @author Sunil Babu Shrestha on 3/18/2020
 */
public interface UserService extends CoreService<User> {
    User save(User user);

    User saveNewUser(User user) throws MessagingException;

    List<User> findAll();

    void deleteById(long id);

    User findById(long id);

    List<User> findUsersByDestination(String destination);

    User getUserByUsername(String email);

    User checkUserExist(User user);

    List<Role> findAllRole();

    Role findRoleByName(String role);
}
