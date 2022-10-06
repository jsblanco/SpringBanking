package com.jsblanco.springbanking.models.products;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jsblanco.springbanking.models.interfaces.HasBalance;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.util.Money;
import jakarta.persistence.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "product_type")
public abstract class BankProduct implements HasBalance {
    @Id
    @GeneratedValue
    private Integer id;
    private BigDecimal amount;
    private Currency currency = Currency.getInstance("USD");

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "primary_owner", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AccountHolder primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondary_owner", nullable = true)
    private AccountHolder secondaryOwner;

    @Transient
    final BigDecimal penaltyFee = new BigDecimal("40");

    public BankProduct() {
    }

    public BankProduct(Integer id, BigDecimal amount, AccountHolder primaryOwner) {
        this.id = id;
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
        this.primaryOwner = primaryOwner;
    }

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
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
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
        if ((secondaryOwner != null) && secondaryOwner.equals(this.primaryOwner)) setSecondaryOwner(null);
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

    @Override
    public String toString() {
        return id.toString()
                + amount.toString()
                + currency.toString()
                + primaryOwner.toString()
                + (secondaryOwner == null ? "" : secondaryOwner.toString());

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BankProduct product)
            return id.equals(product.getId())
                    && amount.equals(product.getAmount())
                    && currency.equals(product.getCurrency())
                    && primaryOwner.equals(product.getPrimaryOwner())
                    && (Objects.equals(secondaryOwner == null ? null : secondaryOwner, product.getSecondaryOwner()));
        return false;
    }
}
