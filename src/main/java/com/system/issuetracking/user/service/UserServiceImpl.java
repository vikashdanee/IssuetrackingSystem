package com.system.issuetracking.user.service;


import com.system.issuetracking.entity.EmailTemplate;
import com.system.issuetracking.issue.model.Issue;
import com.system.issuetracking.user.model.Role;
import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.repository.RoleRepository;
import com.system.issuetracking.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Random;


@Service
@AllArgsConstructor
@SuppressWarnings("Duplicates")
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private JavaMailSender javaMailSender;

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
    public User saveNewUser(User user) throws MessagingException {
        Random random = new Random();
        String rawPassword = String.valueOf(random.nextInt(100000));
        String encryptedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encryptedPassword);
        sendNotification(user,rawPassword);
        return userRepository.save(user);
    }

    public void sendNotification(User user,String password) throws MessagingException {
        String subject="New User Account";
        String body="<p>New user account has been created for this email address. "
                +"Please, use following temporary password for login.</p>"
                +"<p>"+password+"</p>"
                +"<p> Note: Do not forgot to change password once logged in!</p>";
        EmailTemplate emailTemplate = new EmailTemplate(user.getFirstName(),body,"");

        MimeMessage msg = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(emailTemplate.returnContent(), true);
        javaMailSender.send(msg);
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
