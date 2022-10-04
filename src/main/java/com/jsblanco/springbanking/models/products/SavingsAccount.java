package com.jsblanco.springbanking.models.products;

import com.jsblanco.springbanking.models.interfaces.HasInterestRate;
import com.jsblanco.springbanking.models.interfaces.HasMinimumBalance;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.util.DateUtils;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.models.util.Status;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@DiscriminatorValue("savings_account")
public class SavingsAccount extends Account implements HasInterestRate, HasMinimumBalance {

    @NonNull
    @DecimalMax(value="0.5000")
    @DecimalMin(value="0.0001")
    @Digits(integer = 1, fraction = 4)
    private BigDecimal interestRate;
    @NonNull
    @DecimalMin(value="100.00")
    private BigDecimal minimumAmount;
    @NonNull
    private Date lastAccess;

    @Transient
    private static final BigDecimal defaultMinimumAmount = new BigDecimal("1000.00");
    @Transient
    private static final BigDecimal defaultInterestRate = new BigDecimal("0.0025");

    public SavingsAccount() {
        setLastAccess(new Date());
        setMinimumAmount(defaultMinimumAmount);
        setInterestRate(defaultInterestRate);
    }

    public SavingsAccount(Integer id, BigDecimal amount, AccountHolder primaryOwner, String secretKey, Date creationDate, Status status) {
        super(id, amount, primaryOwner, secretKey, creationDate, status);
        setLastAccess(new Date());
        setMinimumAmount(defaultMinimumAmount);
        setInterestRate(defaultInterestRate);
    }

    public void setBalance(Money newBalance) {
        super.setBalance(newBalance);
    }

    public void decreaseBalance(Money deposit) {
        super.setBalance(reduceBalanceAccountingForPenalty(getBalance(), deposit, getPenaltyFee()));
    }

    public Money getMinimumBalance() {
        return new Money(minimumAmount, getCurrency());
    }

    public void setMinimumBalance(Money balance) {
        checkCurrency(balance.getCurrency());
        setMinimumAmount(balance.getAmount());
    }

    public void setMinimumAmount(BigDecimal minimumAmount) {
        this. minimumAmount = minimumAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public int getOverduePeriods(Date lastAccess) {
        Date today = DateUtils.today();
        return DateUtils.getPeriodBetweenDates(lastAccess, today).getYears();
    }

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

    public BigDecimal getDefaultInterestRate() {
        return defaultInterestRate;
    }


    @Override
    public String toString() {
        return super.toString()
                + lastAccess.getTime()
                + minimumAmount
                + interestRate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SavingsAccount account) {
            return super.equals(obj)
                    && lastAccess.equals(account.getLastAccess())
                    && interestRate.equals(account.getInterestRate())
                    && minimumAmount.equals(account.getMinimumBalance().getAmount());
        }
        return false;
    }
}
