package com.jsblanco.springbanking.model.accounts;

import com.jsblanco.springbanking.model.interfaces.HasInterestRate;
import com.jsblanco.springbanking.model.users.AccountHolder;
import com.jsblanco.springbanking.model.interfaces.HasBalance;
import com.jsblanco.springbanking.model.interfaces.HasPenaltyFee;
import com.jsblanco.springbanking.model.interfaces.IsOwned;
import com.jsblanco.springbanking.model.util.Money;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.Currency;

@Entity
public class CreditCard extends Account implements HasPenaltyFee, IsOwned, HasBalance, HasInterestRate {

    @Id
    @GeneratedValue
    private Integer id;
    private BigDecimal creditLimit;
    private BigDecimal interestRate;
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

    public Money getCreditLimit() {
        return new Money(creditLimit, getCurrency());
    }

    public void setCreditLimit(Money creditLimit) {
        if (getCurrency() != creditLimit.getCurrency())
            throw new IllegalArgumentException("This account uses " + getCurrency() + " but you tried to input " + creditLimit.getCurrency() + ".");
        this.creditLimit = creditLimit.getAmount();
    }

    public Money getInterestRate() {
        return new Money(interestRate, getCurrency());
    }

    public void setInterestRate(Money interestRate) {
        if (getCurrency() != interestRate.getCurrency())
            throw new IllegalArgumentException("This account uses " + getCurrency() + " but you tried to input " + interestRate.getCurrency() + ".");
        this.interestRate = interestRate.getAmount();
    }
}
