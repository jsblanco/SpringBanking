package com.jsblanco.springbanking.models.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class User {
    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    private String name;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public boolean areNamesDifferent(String comparedName) {
        return name.trim().toLowerCase().compareTo(comparedName.trim().toLowerCase()) != 0;
    }

    @Override
    public String toString() {
        return id + name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User user) {
            return id.equals(user.getId()) && name.equals(user.getName());
        }
        return false;
    }
}
