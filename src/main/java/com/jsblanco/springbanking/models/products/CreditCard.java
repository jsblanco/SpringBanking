package com.jsblanco.springbanking.models.products;

import com.jsblanco.springbanking.models.interfaces.HasInterestRate;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.util.DateUtils;
import com.jsblanco.springbanking.models.util.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Entity
@DiscriminatorValue("credit_card")
public class CreditCard extends BankProduct implements HasInterestRate {

    @NonNull
    @DecimalMax(value = "100000.00")
    private BigDecimal creditLimit;
    @NonNull
    @DecimalMin(value = "0.2000")
    @DecimalMin(value = "0.1000")
    @Digits(integer = 1, fraction = 4)
    @Column(precision = 1, scale = 4)
    private BigDecimal interestRate;
    @NonNull
    private Date lastMaintenanceDate;

    @Transient
    private static final BigDecimal defaultInterestRate = new BigDecimal("0.2000");
    @Transient
    private static final BigDecimal minInterestRate = new BigDecimal("0.1000");
    @Transient
    private static final BigDecimal defaultCreditLimit = new BigDecimal("100.00");
    @Transient
    private static final BigDecimal maxCreditLimit = new BigDecimal("100000.00");

    public CreditCard() {
        setCreditLimit(new Money(defaultCreditLimit));
        setInterestRate(defaultInterestRate);
        this.lastMaintenanceDate = DateUtils.today();
    }

    public CreditCard(Integer id, BigDecimal amount, AccountHolder primaryOwner) {
        super(id, amount, primaryOwner);
        setCreditLimit(new Money(defaultCreditLimit));
        setInterestRate(defaultInterestRate);
        this.lastMaintenanceDate = DateUtils.today();
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

    public void setCreditLimit(@NonNull BigDecimal creditLimit) {
        if (maxCreditLimit.compareTo(creditLimit) < 0)
            throw new IllegalArgumentException("Credit limit cannot be bigger than " + maxCreditLimit);
        this.creditLimit = creditLimit;
    }

    @NonNull
    public Date getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(@NonNull Date lastMaintenanceDate) {
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
                    && creditLimit.equals(card.getCreditLimit().getAmount())
                    && interestRate.equals(card.getInterestRate())
                    && lastMaintenanceDate.equals(card.lastMaintenanceDate);
        }
        return false;
    }
}
