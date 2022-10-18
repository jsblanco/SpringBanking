package com.jsblanco.springbanking.models.products;

import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.util.Status;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Bank product subclass with all properties common to all accounts which Credit Cards lack.
 */
@Entity
@DiscriminatorValue("bank_account")
public abstract class Account extends BankProduct {

    @NotNull
    private String secretKey;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Transient
    final BigDecimal penaltyFee = new BigDecimal("40");

    public Account() {
    }

    public Account(Integer id, BigDecimal amount, AccountHolder primaryOwner, @NotNull String secretKey, @NotNull Date creationDate, Status status) {
        super(id, amount, primaryOwner, creationDate);
        this.secretKey = secretKey;
        this.status = status;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return super.toString()
                + secretKey
                + getCreationDate()
                + status;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account account)
            return super.equals(obj)
                    && secretKey.equals(account.getSecretKey())
                    && getCreationDate().equals(account.getCreationDate())
                    && status.equals(account.getStatus());
        return false;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }
}
