package com.jsblanco.springbanking.models.products;

import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.models.util.Status;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

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
