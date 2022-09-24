package com.jsblanco.springbanking.model.accounts;

import com.jsblanco.springbanking.model.interfaces.HasInterestRate;
import com.jsblanco.springbanking.model.util.Money;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("credit_card")
public class CreditCard extends BankProduct implements HasInterestRate {

    private BigDecimal creditLimit;
    private BigDecimal interestRate;

    @Transient
    BigDecimal minInterestRate = new BigDecimal("0.1");
    @Transient
    BigDecimal maxCreditLimit = new BigDecimal("100000");

    public CreditCard() {
        setCreditLimit(new Money(new BigDecimal("100")));
        setInterestRate(new BigDecimal("0.1"));
    }

    public Money getCreditLimit() {
        return new Money(creditLimit, getCurrency());
    }

    public void setCreditLimit(Money creditLimit) {
        if (getCurrency() != creditLimit.getCurrency())
            throw new IllegalArgumentException("This account uses " + getCurrency() + " but you tried to input " + creditLimit.getCurrency() + ".");
        if (creditLimit.getAmount().compareTo(maxCreditLimit)>0)
            throw new IllegalArgumentException("Credit limit cannot be higher than " + maxCreditLimit);
        this.creditLimit = creditLimit.getAmount();
    }

    public Money getInterestRate() {
        return new Money(interestRate, getCurrency());
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate.compareTo(minInterestRate) < 0)
            throw new IllegalArgumentException("Interest rate cannot fall beneath " + minInterestRate);
        this.interestRate = interestRate;
    }
}
