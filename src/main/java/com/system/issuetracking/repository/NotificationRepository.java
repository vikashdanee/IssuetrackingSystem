package com.system.issuetracking.repository;

import com.system.issuetracking.issue.model.Issue;
import com.system.issuetracking.issue.model.IssueComment;
import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.model.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<UserNotification, Long> {

    @Query("SELECT n FROM UserNotification  n where n.user = :user and n.stillActive = :stillActive order by n.date desc ")
    List<UserNotification> getNotifications(@Param("user") User user,@Param("stillActive") boolean stillActive);

    @Query("SELECT n FROM UserNotification  n where n.issueId = :id and n.user = :user and n.stillActive = :stillActive")
    UserNotification getNotification(@Param("id") Long id,@Param("user") User user,@Param("stillActive") boolean stillActive);
}
