package com.jsblanco.springbanking.models.products;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jsblanco.springbanking.models.interfaces.HasMaintenanceFee;
import com.jsblanco.springbanking.models.interfaces.HasMinimumBalance;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.util.DateUtils;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.models.util.Status;
import javax.validation.constraints.NotNull;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@DiscriminatorValue("checking_account")
public class CheckingAccount extends Account implements HasMinimumBalance, HasMaintenanceFee {


    @NotNull
    private Date lastMaintenanceDate;
    @Transient
    private static final BigDecimal monthlyMaintenanceFee = new BigDecimal("12.00");
    @Transient
    private static final BigDecimal minimumAmount = new BigDecimal("250.00");

    public CheckingAccount() {
    }

    public CheckingAccount(Integer id, BigDecimal amount, AccountHolder primaryOwner, String secretKey, Date creationDate, Status status) {
        super(id, amount, primaryOwner, secretKey, creationDate, status);
        this.lastMaintenanceDate = DateUtils.today();
    }

    public void decreaseBalance(Money deposit) {
        super.setBalance(reduceBalanceAccountingForPenalty(getBalance(), deposit, penaltyFee));
    }

    @JsonIgnore
    public Money getMinimumBalance() {
        return new Money(minimumAmount, getCurrency());
    }

    @Override
    @JsonIgnore
    public Money getMonthlyMaintenanceFee() {
        return new Money(monthlyMaintenanceFee, getCurrency());
    }

    public int getOverduePeriods(Date lastAccess) {
        Date today = DateUtils.today();
        return DateUtils.getPeriodBetweenDates(lastAccess, today).getMonths()
                + (DateUtils.getPeriodBetweenDates(lastAccess, today).getYears() * 12);
    }

    @NotNull
    public Date getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(@NotNull Date lastMaintenanceDate) {
        if (this.lastMaintenanceDate == null)
            this.lastMaintenanceDate = lastMaintenanceDate;

        int overduePeriods = getOverduePeriods(lastMaintenanceDate);
        if (overduePeriods > 0) {
            setBalance(new Money(subtractMaintenance(monthlyMaintenanceFee, overduePeriods), getCurrency()));
            this.lastMaintenanceDate = DateUtils.today();
        }
    }

    @Override
    public String toString() {
        return super.toString()
                + lastMaintenanceDate.getTime()
                + monthlyMaintenanceFee
                + minimumAmount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CheckingAccount account) {
            return super.equals(obj)
                    && lastMaintenanceDate.equals(account.getLastMaintenanceDate())
                    && monthlyMaintenanceFee.equals(account.getMonthlyMaintenanceFee().getAmount())
                    && minimumAmount.equals(account.getMinimumBalance().getAmount());
        }
        return false;
    }
}
