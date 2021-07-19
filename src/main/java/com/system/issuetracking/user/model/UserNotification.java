package com.system.issuetracking.user.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "user_notifications")
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    public String notification;

    @Column
    private Long issueId;

    @Column
    private Date date;

    @Column
    private Boolean stillActive;

    @ManyToOne
    @JoinColumn
    private User user;
}
