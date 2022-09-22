package com.jsblanco.springbanking.model.accounts;

import com.jsblanco.springbanking.model.interfaces.HasInterestRate;
import com.jsblanco.springbanking.model.util.Money;
import jakarta.persistence.Entity;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Entity
public class SavingsAccount extends Account implements HasInterestRate {

    @NonNull
    private BigDecimal interestRate;

    public Money getInterestRate() {
        return new Money(interestRate, getCurrency());
    }

    public void setInterestRate(Money interestRate) {
        if (getCurrency() != interestRate.getCurrency())
            throw new IllegalArgumentException("This account uses " + getCurrency() + " but you tried to input " + interestRate.getCurrency() + ".");
        this.interestRate = interestRate.getAmount();
    }

}
