package com.jsblanco.springbanking.models.products;

import com.jsblanco.springbanking.models.interfaces.HasInterestRate;
import com.jsblanco.springbanking.models.interfaces.HasMinimumBalance;
import com.jsblanco.springbanking.models.util.DateUtils;
import com.jsblanco.springbanking.models.util.Money;
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
    private BigDecimal minimumAmount;
    @NonNull
    private Date lastAccess;

    @Transient
    private static final BigDecimal defaultMinimumAmount = new BigDecimal("1000");
    @Transient
    private static final BigDecimal defaultInterestRate = new BigDecimal("0.0025");
    @Transient
    private static final BigDecimal maxInterestRate = new BigDecimal("0.5");
    @Transient
    private static final BigDecimal minInterestRate = new BigDecimal("0");
    @Transient
    private static final BigDecimal minMinimumAccount = new BigDecimal("100");

    public SavingsAccount() {
        setLastAccess(new Date());
        setMinimumAmount(defaultMinimumAmount);
        setInterestRate(defaultInterestRate);
    }

    @Override
    public void setBalance(Money newBalance) {
        if (newBalance.getAmount().compareTo(minMinimumAccount) < 0)
            throw new IllegalArgumentException("Balance cannot fall under 100 " + getCurrency());
        super.setBalance(newBalance);
    }

//    @Override
//    public Money calculateBalanceAccountingForPenalty(Money subtractedAmount) {
//        Money finalBalance = getBalance();
//        finalBalance.decreaseAmount(subtractedAmount);
//        if (finalBalance.getAmount().compareTo(getMinimumAmount()) < 0)
//            finalBalance.decreaseAmount(getPenaltyFee());
//
//        return finalBalance;
//    }

    @Override
    public void decreaseBalance(Money deposit) {
        super.setBalance(reduceBalanceAccountingForPenalty(getBalance(), deposit, getPenaltyFee()));
    }

    @Override
    public Money getMinimumBalance() {
        return new Money(minimumAmount, getCurrency());
    }

    @Override
    public void setMinimumBalance(Money balance) {
        checkCurrency(balance.getCurrency());
        setMinimumAmount(balance.getAmount());
    }

    @Override
    public BigDecimal getMinimumAmount(){
        return this.minimumAmount;
    }

    @Override
    public void setMinimumAmount(BigDecimal minimumAmount) {
        if (minimumAmount.compareTo(minMinimumAccount) < 0)
            throw new IllegalArgumentException("Minimum balance fall beneath " + minMinimumAccount);
        this. minimumAmount = minimumAmount;
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
    public int getOverduePeriods(Date lastAccess) {
        Date today = DateUtils.today();
        return DateUtils.getPeriodBetweenDates(lastAccess, today).getYears();
    }

    @Override
    public void chargeInterestIfApplies(Date lastAccess) {
        int overduePeriods = getOverduePeriods(lastAccess);
        if (overduePeriods > 0)
            setBalance(new Money(addInterest(getAmount(), interestRate, overduePeriods), getCurrency()));
    }

    @NonNull
    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(@NonNull Date lastAccess) {
        chargeInterestIfApplies(lastAccess);
        this.lastAccess = DateUtils.today();
    }

    public BigDecimal getMaxInterestRate() {
        return maxInterestRate;
    }

    public BigDecimal getMinInterestRate() {
        return minInterestRate;
    }

    public BigDecimal getMinMinimumAccount() {
        return minMinimumAccount;
    }

    public BigDecimal getDefaultMinimumAmount() {
        return defaultMinimumAmount;
    }

    public BigDecimal getDefaultInterestRate() {
        return defaultInterestRate;
    }
}
