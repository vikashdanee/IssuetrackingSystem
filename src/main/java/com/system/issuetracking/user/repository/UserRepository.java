package com.system.issuetracking.user.repository;

import com.system.issuetracking.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Samrica on 3/18/2020
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u from User u where u.email = ?1")
    User checkUserExists(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User getUserByUsername(@Param("email") String email);

    @Query("SELECT u FROM User  u where u.designation = :designation")
    List<User> getUsersByDesignation(@Param("designation") String designation);
}
