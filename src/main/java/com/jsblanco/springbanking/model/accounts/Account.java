package com.jsblanco.springbanking.model.accounts;

import com.jsblanco.springbanking.model.util.Money;
import com.jsblanco.springbanking.model.util.Status;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

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
}
