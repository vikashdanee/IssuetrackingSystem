package com.system.issuetracking;

import com.system.issuetracking.entity.EmailTemplate;
import com.system.issuetracking.enums.UserType;
import com.system.issuetracking.issue.model.Issue;
import com.system.issuetracking.repository.IssueRepository;
import com.system.issuetracking.user.model.Role;
import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.repository.RoleRepository;
import com.system.issuetracking.user.repository.UserRepository;
import com.system.issuetracking.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserAdminTest {

    @Autowired
    private RoleRepository repo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JavaMailSender javaMailSender;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testCreateRoles() {
        Role user = new Role("User");
        Role admin = new Role("Admin");

        List<Role> roles = new ArrayList<>();
        roles.add(user);
        roles.add(admin);

        repo.saveAll(roles);

        List<Role> listRoles = repo.findAll();

        assertThat(listRoles.size()).isEqualTo(2);
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setEmail("rojina@gmail.com");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setFirstName("Rojina");
        user.setLastName("KC");
        user.setDesignation("Developer");

        User savedUser = userRepository.save(user);

        User existUser = entityManager.find(User.class, savedUser.getId());

        assertThat(user.getEmail()).isEqualTo(existUser.getEmail());

    }

    @Test
    public void testAddRoleToNewUser() {
        Role roleAdmin = repo.findByName("Admin");

        User user = new User();
        user.setEmail("admin@cosmos.edu");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setFirstName("Cosmos");
        user.setLastName("College");
        user.setDesignation("Scrum Master");
        user.addRole(roleAdmin);

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getRoles().size()).isEqualTo(1);
    }

    @Test
    public void testAddRoleToExistingUser() {
        User user = userRepository.findById(1L).get();
        Role roleUser = repo.findByName("User");
        user.addRole(roleUser);

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getRoles().size()).isEqualTo(1);
    }


    @Test
    public void testUserAndAssignIssue() {
        User user = new User();
        user.setEmail("samrica@gmail.com");
        user.setPassword(passwordEncoder.encode("sam123"));
        user.setFirstName("Samrica");
        user.setLastName("Rajbhandari");
        user.setDesignation("Developer");
        Role roleUser = repo.findByName("User");
        user.addRole(roleUser);

        User savedUser = userRepository.save(user);


        Issue issue = new Issue();

        issue.setRequestNo(1);
        issue.setPrepareDate(new Date());
        issue.setTitle("New Chat Feature required");
        issue.setPurposeOfReq("Client want customer chat service in their portal");
        issue.setDesignDetail("Create chat board where user can communicate with other user. Their communication required to be stored " +
                "and retrieve if needed.");
        issue.setPriority("NORMAL");
        issue.setStatus("Assigned");
        issue.setSystemModule("Project A");
        issue.setWorkType("Development");
        issue.setAssignTo(savedUser);

        Issue savedIssue = issueRepository.save(issue);

        assertThat(savedUser.getEmail()).isEqualTo(savedIssue.getAssignTo().getEmail());

    }


    @Test
    public void testSendNotification() throws MessagingException {
        String subject="Issue IT-12 has been assigned to you.";
        String body="<p>Following issue has been assigned to you.</p>"
                +"<p>Title : User Chat Room</p>"
                +"<p>Design Detail: Create User communication room and store their communication record</p>";
        EmailTemplate emailTemplate = new EmailTemplate("daneevikas22@gmail.com",body,"");

        MimeMessage msg = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo("daneevikas22@gmail.com");

        helper.setSubject(subject);

        helper.setText("THIS IS BODY", true);

        javaMailSender.send(msg);
    }



}
