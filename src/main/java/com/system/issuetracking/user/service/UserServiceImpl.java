package com.system.issuetracking.user.service;


import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Sunil Babu Shrestha on 3/18/2020
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findById(long id) {

        return userRepository.getOne(id);
    }

    @Override
    public User checkUserExist(User user) {
        User newUer = userRepository.checkUserExists(user.getEmail());
        if(newUer!=null && passwordEncoder.matches(user.getPassword(),newUer.getPassword()))
            return newUer;
        else
            return null;
    }

    @Override
    public User save(User user) {
        String rawPassword = user.getPassword();
        String encryptedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encryptedPassword);
        return userRepository.save(user);
    }
}
