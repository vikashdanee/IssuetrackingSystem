package com.system.issuetracking.repository;

import com.system.issuetracking.issue.model.Issue;
import com.system.issuetracking.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("SELECT i FROM Issue  i where i.status = :status order by i.requestNo asc")
    List<Issue> getIssuesByStatus(@Param("status") String status);

    @Query("SELECT i FROM Issue  i where i.assignTo = :user order by i.requestNo asc")
    List<Issue> getIssuesByUser(@Param("user") User user);

    @Query("SELECT i FROM Issue  i where i.assignTo = :user and i.status = :status order by i.requestNo asc")
    List<Issue> getIssuesByUserAndStatus(@Param("user") User user,@Param("status") String status);

    @Query("SELECT i FROM Issue  i where i.requestNo = :requestNo")
    Issue getIssueByRequestNo(@Param("requestNo") int requestNo);

    @Query("select i from Issue i where  i.requestNo= (select  max(r.requestNo) from Issue r)")
    Issue getRequestNumber();
}
