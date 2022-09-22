package com.jsblanco.springbanking.model.users;

import com.jsblanco.springbanking.model.Address;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class AccountHolder extends User {
    private Date birthDay;
    @OneToOne
    @JoinColumn(name = "primary_address", nullable = false)
    private Address primaryAddress;
    @OneToOne
    @JoinColumn(name = "mailing_address", nullable = true)
    private Address mailingAddress;

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
}
