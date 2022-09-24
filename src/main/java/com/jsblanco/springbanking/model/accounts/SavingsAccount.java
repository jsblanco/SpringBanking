package com.jsblanco.springbanking.model.accounts;

import com.jsblanco.springbanking.model.interfaces.HasInterestRate;
import com.jsblanco.springbanking.model.interfaces.HasMinimumBalance;
import com.jsblanco.springbanking.model.util.DateUtils;
import com.jsblanco.springbanking.model.util.Money;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@DiscriminatorValue("savings_account")
public class SavingsAccount extends Account implements HasInterestRate, HasMinimumBalance {

    @NonNull
    private BigDecimal interestRate;
    @NonNull
    private BigDecimal minimumBalance;
    @NonNull
    private Date lastAccess;

    @Transient
    BigDecimal maxInterestRate = new BigDecimal("0.5");
    @Transient
    BigDecimal minInterestRate = new BigDecimal("0");
    @Transient
    BigDecimal minMinimumAccount = new BigDecimal("100");

    public SavingsAccount() {
        setMinimumAmount(new BigDecimal(1000));
        setInterestRate(new BigDecimal("0.0025"));
        setLastAccess(new Date());
    }

    public Money getMinimumBalance() {
        return new Money(minimumBalance, getCurrency());
    }

    @Override
    public Money applyPenaltyIfNewBalanceIsBelowMinimum(Money substractedAmount) {
        Money finalBalance = getBalance();
        finalBalance.decreaseAmount(substractedAmount);
        if (finalBalance.getAmount().compareTo(getMinimumAmount()) < 0)
            finalBalance.decreaseAmount(getPenaltyFee());

        return finalBalance;
    }

    @Override
    public void substractBalance(Money deposit) {
        super.setBalance(applyPenaltyIfNewBalanceIsBelowMinimum(deposit));
    }

    @Override
    public void setMinimumAmount(BigDecimal minimumAmount) {
        if (interestRate.compareTo(minMinimumAccount) < 0)
            throw new IllegalArgumentException("Minimum balance fall beneath " + minMinimumAccount);
        super.setMinimumAmount(minimumAmount);
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate.compareTo(maxInterestRate) > 0)
            throw new IllegalArgumentException("The maximum interest rate for savings accounts is " + maxInterestRate);
        if (interestRate.compareTo(minInterestRate) < 0)
            throw new IllegalArgumentException("Interest rate cannot fall under " + minInterestRate);
        this.interestRate = interestRate;
    }

    @Override
    public void mustInterestBeCharged() {
        Date today = new Date();
        int overduePeriods = DateUtils.getPeriodBetweenDates(getLastAccess(), today).getYears();
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


    public void setMinimumBalance(@NonNull BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    @NonNull
    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(@NonNull Date lastAccess) {
        this.lastAccess = lastAccess;
        mustInterestBeCharged();
    }

    public BigDecimal getMaxInterestRate() {
        return maxInterestRate;
    }

    public void setMaxInterestRate(BigDecimal maxInterestRate) {
        this.maxInterestRate = maxInterestRate;
    }

    public BigDecimal getMinInterestRate() {
        return minInterestRate;
    }

    public void setMinInterestRate(BigDecimal minInterestRate) {
        this.minInterestRate = minInterestRate;
    }

    public BigDecimal getMinMinimumAccount() {
        return minMinimumAccount;
    }

    public void setMinMinimumAccount(BigDecimal minMinimumAccount) {
        this.minMinimumAccount = minMinimumAccount;
    }
}
