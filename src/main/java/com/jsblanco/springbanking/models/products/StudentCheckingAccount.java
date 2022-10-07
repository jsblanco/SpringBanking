package com.jsblanco.springbanking.models.products;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("student_checking_account")
public class StudentCheckingAccount extends Account {

    public StudentCheckingAccount() {
    }

    public StudentCheckingAccount(CheckingAccount checkingAccount) {
        setId(checkingAccount.getId());
        setStatus(checkingAccount.getStatus());
        setBalance(checkingAccount.getBalance());
        setSecretKey(checkingAccount.getSecretKey());
        setCreationDate(checkingAccount.getCreationDate());
        setPrimaryOwner(checkingAccount.getPrimaryOwner());
        setSecondaryOwner(checkingAccount.getSecondaryOwner());
    }
}
