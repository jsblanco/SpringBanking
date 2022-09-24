package com.jsblanco.springbanking.model.accounts;

import com.jsblanco.springbanking.model.interfaces.HasInterestRate;
import com.jsblanco.springbanking.model.util.DateUtils;
import com.jsblanco.springbanking.model.util.Money;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@DiscriminatorValue("credit_card")
public class CreditCard extends BankProduct implements HasInterestRate {

    @NonNull
    private BigDecimal creditLimit;
    @NonNull
    private BigDecimal interestRate;
    @NonNull
    private Date lastAccess;

    @Transient
    BigDecimal minInterestRate = new BigDecimal("0.1");
    @Transient
    BigDecimal maxCreditLimit = new BigDecimal("100000");

    public CreditCard() {
        setCreditLimit(new Money(new BigDecimal("100")));
        setInterestRate(new BigDecimal("0.1"));
        setLastAccess(new Date());
    }

    public Money getCreditLimit() {
        return new Money(creditLimit, getCurrency());
    }

    public void setCreditLimit(Money creditLimit) {
        if (getCurrency() != creditLimit.getCurrency())
            throw new IllegalArgumentException("This account uses " + getCurrency() + " but you tried to input " + creditLimit.getCurrency() + ".");
        if (creditLimit.getAmount().compareTo(maxCreditLimit) > 0)
            throw new IllegalArgumentException("Credit limit cannot be higher than " + maxCreditLimit);
        this.creditLimit = creditLimit.getAmount();
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate.compareTo(minInterestRate) < 0)
            throw new IllegalArgumentException("Interest rate cannot fall beneath " + minInterestRate);
        this.interestRate = interestRate;
    }

    @Override
    public void mustInterestBeCharged() {
        Date today = new Date();
        int overduePeriods = DateUtils.getPeriodBetweenDates(getLastAccess(), today).getMonths();
        if (overduePeriods > 0)
            substractBalance(calculateInterest(overduePeriods));
        setLastAccess(today);
    }

    @Override
    public Money calculateInterest(int overduePeriods) {
        return new Money(getAmount().multiply(
                interestRate.multiply(new BigDecimal(overduePeriods)).add(new BigDecimal("1"))),
                getCurrency());
    }

    public void setCreditLimit(@NonNull BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    @NonNull
    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(@NonNull Date lastAccess) {
        this.lastAccess = lastAccess;
        mustInterestBeCharged();
    }

    public BigDecimal getMinInterestRate() {
        return minInterestRate;
    }

    public void setMinInterestRate(BigDecimal minInterestRate) {
        this.minInterestRate = minInterestRate;
    }

    public BigDecimal getMaxCreditLimit() {
        return maxCreditLimit;
    }

    public void setMaxCreditLimit(BigDecimal maxCreditLimit) {
        this.maxCreditLimit = maxCreditLimit;
    }
}
