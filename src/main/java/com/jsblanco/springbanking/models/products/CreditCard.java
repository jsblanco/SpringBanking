package com.jsblanco.springbanking.models.products;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jsblanco.springbanking.models.interfaces.HasInterestRate;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.util.DateUtils;
import com.jsblanco.springbanking.models.util.Money;

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
@JsonIgnoreProperties(ignoreUnknown = true)
@DiscriminatorValue("credit_card")
public class CreditCard extends BankProduct implements HasInterestRate {

    @NotNull
    @DecimalMax(value = "100000.00")
    private BigDecimal creditLimit;
    @NotNull
    @DecimalMax(value = "0.2000")
    @DecimalMin(value = "0.1000")
    @Digits(integer = 1, fraction = 4)
    @Column(precision = 1, scale = 4)
    private BigDecimal interestRate;
    @NotNull @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-mm-dd")
    private Date lastMaintenanceDate;

    @Transient @JsonIgnore
    private static final BigDecimal defaultInterestRate = new BigDecimal("0.2000");
    @Transient @JsonIgnore
    private static final BigDecimal minInterestRate = new BigDecimal("0.1000");
    @Transient @JsonIgnore
    private static final BigDecimal defaultCreditLimit = new BigDecimal("100.00");
    @Transient @JsonIgnore
    private static final BigDecimal maxCreditLimit = new BigDecimal("100000.00");

    public CreditCard() {
        setCreditLimit(defaultCreditLimit);
        setInterestRate(defaultInterestRate);
        this.lastMaintenanceDate = DateUtils.today();
    }

    public CreditCard(Integer id, BigDecimal amount, AccountHolder primaryOwner) {
        super(id, amount, primaryOwner);
        setCreditLimit(defaultCreditLimit);
        setInterestRate(defaultInterestRate);
        this.lastMaintenanceDate = DateUtils.today();
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    @JsonIgnore
    public void setCreditLimit(Money creditLimit) {
        if (getCurrency() != creditLimit.getCurrency())
            throw new IllegalArgumentException("This account uses " + getCurrency() + " but you tried to input " + creditLimit.getCurrency() + ".");
        if (creditLimit.getAmount().compareTo(maxCreditLimit) > 0)
            throw new IllegalArgumentException("Credit limit cannot be higher than " + maxCreditLimit);
        this.creditLimit = creditLimit.getAmount();
    }

    @JsonProperty("creditLimit")
    public void setCreditLimit(@NotNull BigDecimal creditLimit) {
        if (maxCreditLimit.compareTo(creditLimit) < 0)
            throw new IllegalArgumentException("Credit limit cannot be bigger than " + maxCreditLimit);
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate.setScale(4, RoundingMode.HALF_EVEN);
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate.compareTo(defaultInterestRate) > 0)
            throw new IllegalArgumentException("Interest rate cannot be over " + defaultInterestRate);
        if (interestRate.compareTo(minInterestRate) < 0)
            throw new IllegalArgumentException("Interest rate cannot fall beneath " + minInterestRate);
        this.interestRate = interestRate.setScale(4, RoundingMode.HALF_EVEN);
    }

    public int getOverduePeriods(Date lastMaintenanceDate) {
        Date today = DateUtils.today();
        return DateUtils.getPeriodBetweenDates(lastMaintenanceDate, today).getMonths();
    }

    @NotNull
    public Date getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(@NotNull Date lastMaintenanceDate) {
        int overduePeriods = getOverduePeriods(lastMaintenanceDate);
        BigDecimal monthlyInterestRate = getInterestRate().divide(new BigDecimal(12), RoundingMode.HALF_EVEN);
        if (overduePeriods > 0) {
            setBalance(new Money(addInterest(getAmount(), monthlyInterestRate, overduePeriods), getCurrency()));
            this.lastMaintenanceDate = DateUtils.today();
        }
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

    @Override
    public String toString() {
        return super.toString()
                + creditLimit
                + interestRate
                + lastMaintenanceDate.getTime();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CreditCard card) {
            return super.equals(obj)
                    && creditLimit.equals(card.getCreditLimit())
                    && interestRate.equals(card.getInterestRate())
                    && lastMaintenanceDate.equals(card.lastMaintenanceDate);
        }
        return false;
    }
}
