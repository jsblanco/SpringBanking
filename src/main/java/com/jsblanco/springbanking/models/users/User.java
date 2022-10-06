package com.jsblanco.springbanking.models.users;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.springframework.lang.NonNull;

@MappedSuperclass
public abstract class User {
    @Id
    @GeneratedValue
    private Integer id;
    @NonNull
    private String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
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
