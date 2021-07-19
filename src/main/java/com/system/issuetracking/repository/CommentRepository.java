package com.system.issuetracking.repository;

import com.system.issuetracking.issue.model.Issue;
import com.system.issuetracking.issue.model.IssueComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Monica Rajbhandari on 7/17/21
 */

@Repository
public interface CommentRepository extends JpaRepository<IssueComment, Long> {

    @Query("SELECT c FROM IssueComment  c where c.issue = :issue order by c.commentDate desc ")
    List<IssueComment> getIssueComments(@Param("issue") Issue issue);
}
