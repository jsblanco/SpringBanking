package com.jsblanco.springbanking.models.products;

import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.util.DateUtils;
import com.jsblanco.springbanking.models.util.Status;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@DiscriminatorValue("bank_account")
public abstract class Account extends BankProduct {

    @NonNull
    private String secretKey;
    @NonNull
    private Date creationDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Account() {
    }

    public Account(Integer id, BigDecimal amount, AccountHolder primaryOwner, @NonNull String secretKey, @NonNull Date creationDate, Status status) {
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
}
