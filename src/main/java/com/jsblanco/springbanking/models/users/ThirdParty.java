package com.jsblanco.springbanking.models.users;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class ThirdParty extends User {
    @NotNull
    private String hashedKey;

    public ThirdParty() {
    }

    public ThirdParty(String name, String hashedKey) {
        super(name);
        this.hashedKey = hashedKey;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }
}
