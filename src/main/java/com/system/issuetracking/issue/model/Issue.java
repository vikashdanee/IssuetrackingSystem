package com.system.issuetracking.issue.model;

import com.system.issuetracking.user.model.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private int requestNo;

    @Column
    private String systemModule;

    @Column(length = 1000)
    private String purposeOfReq;

    @Column(length = 1000)
    private String title;

    @Column
    private String workType;

    @Column(length = 10000)
    private String designDetail;

    @ManyToOne
    @JoinColumn
    private User prepareBy;

    @Column
    private Date prepareDate;

    @Column
    private String priority;

    @ManyToOne
    @JoinColumn
    private User assignTo;

    @Column
    private Date fixedDate;

    @Column
    private String type;

    @Column
    private String status;

    @Column
    private Date  notLaterThan;

    @Column
    private Date eta;

    @Column
    private Boolean showComment;

    @Column
    private Boolean disabled;

    @Column
    private Boolean hold;

    @Column
    private Boolean relatedIssue;

    @Column
    private Integer previousIssue;

    @Column
    private Integer percentageComplete;

    @Column
    private String remedyMadeExplanation;

    @OneToMany(mappedBy = "issue",cascade = CascadeType.ALL)
    private List<IssueComment> comments;

}
