package com.system.issuetracking.issue.model;

import com.system.issuetracking.user.model.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
@Table
public class ToDoList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 10000)
    public String plan;

    @Column
    private Date prepareDate;

    @Column
    private Boolean completed;

    @ManyToOne
    @JoinColumn
    private User user;
}
