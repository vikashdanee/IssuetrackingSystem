package com.system.issuetracking.user.repository;

import com.system.issuetracking.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Samrica on 3/18/2020
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u from User u where u.email = ?1")
    User checkUserExists(String email);
}
