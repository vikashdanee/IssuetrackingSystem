package com.system.issuetracking.user.service;


import com.system.issuetracking.user.model.Role;
import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.repository.RoleRepository;
import com.system.issuetracking.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;
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
    public List<User> findUsersByDestination(String destination) {
        return userRepository.getUsersByDesignation(destination);
    }

    @Override
    public User getUserByUsername(String email) {
        return userRepository.getUserByUsername(email);
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
        String rawPassword = user.getPassword()==null?"P@ssword":user.getPassword();
        String encryptedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encryptedPassword);
        return userRepository.save(user);
    }

    @Override
    public List<Role> findAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public Role findRoleByName(String role) {
        return roleRepository.findByName(role);
    }
}
