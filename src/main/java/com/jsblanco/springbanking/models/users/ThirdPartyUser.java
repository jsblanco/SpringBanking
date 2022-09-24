package com.jsblanco.springbanking.models.users;

import jakarta.persistence.Entity;

@Entity
public class ThirdPartyUser extends User {
    private String hashedKey;
}
