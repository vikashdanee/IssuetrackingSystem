package com.system.issuetracking.entity;

import javax.mail.MessagingException;

/**
 * @author Sunil Babu Shrestha on 3/18/2020
 */
public interface CoreService<T> {

    T save(T t) throws MessagingException;
}
