package com.system.issuetracking;

import com.system.issuetracking.enums.UserType;
import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Monica Rajbhandari on 7/7/21
 */

@SpringBootTest
public class UserAdminTest {
    @Autowired
    private UserService userService;

    @Test
    void createAdmin(){
        User user = new User();
        user.setEmail("admin@cosmos.edu");
        user.setPassword("admin");
        user.setUserType(UserType.ADMIN);
        userService.save(user);
    }

    @Test
    void checkUserExist(){
        User user = new User();
        user.setEmail("admin@cosmos.edu");
        user.setPassword("admin");
        User newUser = userService.checkUserExist(user);
        System.out.printf(String.valueOf(newUser));

    }
}
