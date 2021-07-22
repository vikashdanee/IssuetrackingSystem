package com.system.issuetracking.service;

import com.system.issuetracking.entity.CoreService;
import com.system.issuetracking.issue.model.Issue;
import com.system.issuetracking.issue.model.IssueComment;
import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.model.UserNotification;

import javax.mail.MessagingException;
import java.util.List;


public interface IssueService extends CoreService<Issue> {

    Issue save(Issue issue) throws MessagingException;

    List<Issue> findAllIssue();

    List<Issue> findAllIssueByUser(User user);

    List<Issue> findAllIssueByUserAndStatus(User user, String status);

    List<Issue> findAllIssueByUserAndPriority(User user, String priority);

    List<Issue> findAllIssueByPriority(String priority);

    List<Issue> findAllIssueByStatus(String status);

    List<UserNotification> findUserNotification(User user, boolean stillActive);

    void deleteCommentById(long id);

    void deleteById(long id);

    Issue findById(long id);

    Issue update(Issue issue) throws MessagingException;

    Issue maskAsCompleted(Issue issue) throws MessagingException;

    Issue reAssigned(Issue issue) throws MessagingException;

    Issue maskAsTest(Issue issue) throws MessagingException;

    List<UserNotification> findNotificationByIssueId(Long id,User user, boolean stillActive);

    UserNotification saveNotification(UserNotification userNotification);

    IssueComment saveComment(IssueComment issueComment);

    List<IssueComment> findAllIssueComments(Issue issue);

}
