package com.jsblanco.springbanking.models.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jsblanco.springbanking.models.interfaces.HasInterestRate;
import com.jsblanco.springbanking.models.interfaces.HasMinimumBalance;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.util.DateUtils;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.models.util.Status;
import javax.validation.constraints.NotNull;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Entity
@DiscriminatorValue("savings_account")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public class SavingsAccount extends Account implements HasInterestRate, HasMinimumBalance {

    @NotNull
    @DecimalMax(value = "0.5000")
    @DecimalMin(value = "0.0001")
    @Digits(integer = 1, fraction = 4)
    @Column(precision = 1, scale = 4)
    private BigDecimal interestRate;
    @NotNull
    @DecimalMin(value = "100.00")
    private BigDecimal minimumAmount;
    @NotNull
    private Date lastMaintenanceDate;

    @Transient @JsonIgnore
    private static final BigDecimal defaultMinimumAmount = new BigDecimal("1000.00");
    @Transient @JsonIgnore
    private static final BigDecimal defaultInterestRate = new BigDecimal("0.0025");
    @Transient @JsonIgnore
    private static final BigDecimal maxInterestRate = new BigDecimal("0.5");
    @Transient @JsonIgnore
    private static final BigDecimal minInterestRate = new BigDecimal("0");
    @Transient @JsonIgnore
    private static final BigDecimal minMinimumAmount = new BigDecimal("100");


    public SavingsAccount() {
        setMinimumAmount(defaultMinimumAmount);
        setInterestRate(defaultInterestRate);
    }

    public SavingsAccount(Integer id, BigDecimal amount, AccountHolder primaryOwner, String secretKey, Date creationDate, Status status) {
        super(id, amount, primaryOwner, secretKey, creationDate, status);
        this.lastMaintenanceDate = DateUtils.today();
        setMinimumAmount(defaultMinimumAmount);
        setInterestRate(defaultInterestRate);
    }

    public void setBalance(Money newBalance) {
        if (newBalance.getAmount().compareTo(minMinimumAmount) < 0)
            throw new IllegalArgumentException("Class cannot be instantiated with balance under " + minMinimumAmount + getCurrency());
        super.setBalance(newBalance);
    }

    public void decreaseBalance(Money deposit) {
        super.setBalance(reduceBalanceAccountingForPenalty(getBalance(), deposit, getPenaltyFee()));
    }

    @JsonIgnore
    public Money getMinimumBalance() {
        return new Money(minimumAmount, getCurrency());
    }

    public void setMinimumBalance(Money balance) {
        checkCurrency(balance.getCurrency());
        setMinimumAmount(balance.getAmount());
    }

    public void setMinimumAmount(BigDecimal minimumAmount) {
        if (minimumAmount.compareTo(minMinimumAmount) < 0)
            throw new IllegalArgumentException("Minimum balance fall beneath " + minMinimumAmount);
        this.minimumAmount = minimumAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate.compareTo(maxInterestRate) > 0)
            throw new IllegalArgumentException("The maximum interest rate for savings accounts is " + maxInterestRate);
        if (interestRate.compareTo(minInterestRate) < 0)
            throw new IllegalArgumentException("Interest rate cannot fall under " + minInterestRate);
        this.interestRate = interestRate.setScale(4, RoundingMode.HALF_EVEN);
    }

    public int getOverduePeriods(Date lastAccess) {
        Date today = DateUtils.today();
        return DateUtils.getPeriodBetweenDates(lastAccess, today).getYears();
    }

    @NotNull
    public Date getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(@NotNull Date lastAccess) {
        if (this.lastMaintenanceDate == null) this.lastMaintenanceDate = lastAccess;
        int overduePeriods = getOverduePeriods(lastAccess);
        if (overduePeriods > 0) {
            setBalance(new Money(addInterest(getAmount(), interestRate, overduePeriods), getCurrency()));
            this.lastMaintenanceDate = DateUtils.today();
        }
    }

    @JsonIgnore
    public BigDecimal getDefaultInterestRate() {
        return defaultInterestRate;
    }

    @Override
    public String toString() {
        return super.toString()
                + lastMaintenanceDate.getTime()
                + minimumAmount
                + interestRate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SavingsAccount account) {
            return super.equals(obj)
                    && lastMaintenanceDate.equals(account.getLastMaintenanceDate())
                    && interestRate.equals(account.getInterestRate())
                    && minimumAmount.equals(account.getMinimumBalance().getAmount());
        }
        return false;
    }
}
