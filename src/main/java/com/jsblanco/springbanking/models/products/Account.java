package com.jsblanco.springbanking.models.products;

import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.util.DateUtils;
import com.jsblanco.springbanking.models.util.Status;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@DiscriminatorValue("bank_account")
public abstract class Account extends BankProduct {

    @NotNull
    private String secretKey;
    @NotNull
    private Date creationDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Transient
    final BigDecimal penaltyFee = new BigDecimal("40");

    public Account() {
    }

    public Account(Integer id, BigDecimal amount, AccountHolder primaryOwner, @NotNull String secretKey, @NotNull Date creationDate, Status status) {
        super(id, amount, primaryOwner);
        this.secretKey = secretKey;
        this.creationDate = DateUtils.round(creationDate);
        this.status = status;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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
                + creationDate
                + status;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account account)
            return super.equals(obj)
                    && secretKey.equals(account.getSecretKey())
                    && creationDate.equals(account.getCreationDate())
                    && status.equals(account.getStatus());
        return false;
    }

    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }
}
