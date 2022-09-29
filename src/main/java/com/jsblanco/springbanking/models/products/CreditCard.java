package com.jsblanco.springbanking.models.products;

import com.jsblanco.springbanking.models.interfaces.HasInterestRate;
import com.jsblanco.springbanking.models.util.DateUtils;
import com.jsblanco.springbanking.models.util.Money;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private static final BigDecimal defaultInterestRate = new BigDecimal("0.10");
    @Transient
    private static final BigDecimal minInterestRate = new BigDecimal("0.10");
    @Transient
    private static final BigDecimal defaultCreditLimit = new BigDecimal("100.00");
    @Transient
    private static final BigDecimal maxCreditLimit = new BigDecimal("100000.00");

    public CreditCard() {
        setCreditLimit(new Money(defaultCreditLimit));
        setInterestRate(defaultInterestRate);
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

    public int getOverduePeriods(Date lastAccess) {
        Date today = DateUtils.today();
        return DateUtils.getPeriodBetweenDates(lastAccess, today).getMonths();
    }

    public void chargeInterestIfApplies(Date lastAccess) {
        int overduePeriods = getOverduePeriods(lastAccess);
        BigDecimal monthlyInterestRate = getInterestRate().divide(new BigDecimal(12), RoundingMode.HALF_EVEN);
        if (overduePeriods > 0)
            setBalance(new Money(addInterest(getAmount(), monthlyInterestRate, overduePeriods), getCurrency()));
    }

    public void setCreditLimit(@NonNull BigDecimal creditLimit) {
        if (maxCreditLimit.compareTo(creditLimit) < 0)
            throw new IllegalArgumentException("Credit limit cannot be bigger than " + maxCreditLimit);
        this.creditLimit = creditLimit;
    }

    @NonNull
    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(@NonNull Date lastAccess) {
        chargeInterestIfApplies(lastAccess);
        this.lastAccess = DateUtils.today();
    }

    public BigDecimal getMinInterestRate() {
        return minInterestRate;
    }


    public BigDecimal getMaxCreditLimit() {
        return maxCreditLimit;
    }

    public BigDecimal getDefaultInterestRate() {
        return defaultInterestRate;
    }

    public BigDecimal getDefaultCreditLimit() {
        return defaultCreditLimit;
    }
}
