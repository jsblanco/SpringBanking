package com.jsblanco.springbanking.model.users;

import jakarta.persistence.Entity;

@Entity
public class ThirdParty extends User {
    private String hashedKey;
}
