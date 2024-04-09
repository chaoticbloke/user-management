package com.users.management.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class User implements Serializable {
//implements Serializable because User entity will go through transition from Java class to Database
    @Id
    @GeneratedValue
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String username;

    private String password;
    private String email;
    private String profileImageUrl;

    private Date lastLoginDate;
    private Date joinDate;
    private Date lastLoginDateDisplay;

    private String[] roles;

    private String[] authorities;

    private boolean isActive;

    private boolean isLocked;
}
