package com.jsblanco.springbanking.models.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jsblanco.springbanking.models.interfaces.HasBalance;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.util.Money;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.Currency;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type")
public abstract class BankProduct implements HasBalance {
    @Id
    @GeneratedValue
    private Integer id;
    private BigDecimal amount;
    private Currency currency = Currency.getInstance("USD");

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "primary_owner", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AccountHolder primaryOwner;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "secondary_owner", nullable = true)
    private AccountHolder secondaryOwner;

    @Transient
    final BigDecimal penaltyFee = new BigDecimal("40");

    public void checkCurrency(Currency currency) {
        if (!currency.equals(getCurrency()))
            throw new IllegalArgumentException("This account used " + getCurrency() + ". Currency must be converted to " + getCurrency() + " before making transactions");
    }

    public Money getBalance() {
        return new Money(amount, currency);
    }

    public void setBalance(Money balance) {
        setAmount(balance.getAmount());
        setCurrency(balance.getCurrency());
    }

    public void increaseBalance(Money deposit) {
        checkCurrency(deposit.getCurrency());

        Money finalBalance = getBalance();
        finalBalance.increaseAmount(deposit);
        setBalance(finalBalance);
    }

    public void decreaseBalance(Money deposit) {
        checkCurrency(deposit.getCurrency());

        Money finalBalance = getBalance();
        finalBalance.decreaseAmount(deposit);
        setBalance(finalBalance);
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
        if (secondaryOwner.equals(this.primaryOwner)) setSecondaryOwner(null);
    }

    @Nullable
    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }

    public void setSecondaryOwner(@Nullable AccountHolder secondaryOwner) {
        if (secondaryOwner == null || !secondaryOwner.equals(this.primaryOwner)) this.secondaryOwner = secondaryOwner;
    }

    @NonNull
    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }
}
