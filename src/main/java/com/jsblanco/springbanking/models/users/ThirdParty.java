package com.jsblanco.springbanking.models.users;

import javax.persistence.Entity;
import org.springframework.lang.NonNull;

@Entity
public class ThirdParty extends User {
    @NonNull
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
