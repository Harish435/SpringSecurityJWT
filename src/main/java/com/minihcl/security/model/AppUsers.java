package com.minihcl.security.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity

public class AppUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userName;
    private String name;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Roles> roles= new ArrayList<>();

    public AppUsers() {
    }

    public AppUsers(Long id, String userName, String name, String password, Collection<Roles> roles) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Roles> roles) {
        this.roles = roles;
    }
}
