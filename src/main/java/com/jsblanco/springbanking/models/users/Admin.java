package com.jsblanco.springbanking.models.users;

import javax.persistence.Entity;

@Entity
public class Admin extends User {
    public Admin() {
    }

    public Admin(String name, String username, String password) {
        super(name, username, password);
    }

}
