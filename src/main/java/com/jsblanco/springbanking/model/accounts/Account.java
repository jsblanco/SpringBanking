package com.jsblanco.springbanking.model.accounts;

import com.jsblanco.springbanking.model.users.AccountHolder;
import com.jsblanco.springbanking.model.interfaces.HasBalance;
import com.jsblanco.springbanking.model.interfaces.IsOwned;
import com.jsblanco.springbanking.model.util.Money;
import com.jsblanco.springbanking.model.util.Status;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

@MappedSuperclass
public abstract class Account implements HasBalance, IsOwned {
    @Id
    @GeneratedValue
    private Integer id;

    private BigDecimal amount;
    private Currency currency;

    @OneToOne
    @JoinColumn(name = "primary_owner", nullable = false)
    @NonNull
    private AccountHolder primaryOwner;

    @OneToOne
    @JoinColumn(name = "secondary_owner", nullable = true)
    @Nullable
    private AccountHolder secondaryOwner;

    @NonNull
    @Transient
    Integer penaltyFee = 40;


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


    public Money getBalance() {
        return new Money(amount, currency);
    }

    public void setBalance(Money balance) {
        setAmount(balance.getAmount());
        setCurrency(balance.getCurrency());
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @NonNull
    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }

    public void setPrimaryOwner(@NonNull AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }

    @Nullable
    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(@Nullable AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }

    @NonNull
    public Integer getPenaltyFee() {
        return penaltyFee;
    }
}
