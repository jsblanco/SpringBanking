package com.jsblanco.springbanking.models.users;

import jakarta.persistence.Entity;

@Entity
public class ThirdParty extends User {
    private String hashedKey;

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }
}
