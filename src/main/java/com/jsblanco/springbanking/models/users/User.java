package com.jsblanco.springbanking.models.users;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.InheritanceType.JOINED;

@Entity
@Inheritance(strategy = JOINED)
@DiscriminatorColumn(name="user_type")
public abstract class User {
    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    @Column(unique = true)
    private String username;
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public User() {
    }

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
