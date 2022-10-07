package com.jsblanco.springbanking.models.users;

import com.jsblanco.springbanking.models.util.Address;
import javax.persistence.*;
import org.springframework.lang.NonNull;

import java.util.*;

@Entity
public class AccountHolder extends User {
    @NonNull
    private Date birthDay;

    @Embedded
    @NonNull
    @AttributeOverrides({
            @AttributeOverride(name = "door", column = @Column(name = "primary_door")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "primary_postal_code")),
            @AttributeOverride(name = "city", column = @Column(name = "primary_city")),
            @AttributeOverride(name = "country", column = @Column(name = "primary_country"))
    })
    private Address primaryAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "door", column = @Column(name = "mailing_door")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "mailing_postal_code")),
            @AttributeOverride(name = "city", column = @Column(name = "mailing_city")),
            @AttributeOverride(name = "country", column = @Column(name = "mailing_country"))
    })
    private Address mailingAddress;


    public AccountHolder() {
    }

    public AccountHolder( String name, @NonNull Date birthDay, @NonNull Address primaryAddress) {
        super(name);
        this.birthDay = birthDay;
        this.primaryAddress = primaryAddress;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Address getMailingAddress() {
        if (mailingAddress == null) return primaryAddress;
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    @Override
    public String toString() {
        return super.toString() + birthDay.getTime() + primaryAddress + (mailingAddress == null ? "" : mailingAddress);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AccountHolder user) {
            return super.equals(user)
                    && birthDay.equals(user.getBirthDay());
        }
        return false;
    }
}
