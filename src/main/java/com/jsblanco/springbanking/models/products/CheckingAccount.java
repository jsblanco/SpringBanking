package com.jsblanco.springbanking.models.products;

import com.jsblanco.springbanking.models.interfaces.HasMaintenanceFee;
import com.jsblanco.springbanking.models.interfaces.HasMinimumBalance;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.util.DateUtils;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.models.util.Status;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@DiscriminatorValue("checking_account")
public class CheckingAccount extends Account implements HasMinimumBalance, HasMaintenanceFee {

    @NonNull
    private Date lastAccess;
    @NonNull
    private final BigDecimal monthlyMaintenanceFee = new BigDecimal("12.00");
    @NonNull
    private final BigDecimal minimumAmount = new BigDecimal("250.00");

    public CheckingAccount() {
    }

    public CheckingAccount(Integer id, BigDecimal amount, AccountHolder primaryOwner, String secretKey, Date creationDate, Status status) {
        super(id, amount, primaryOwner, secretKey, creationDate, status);
        this.lastAccess = DateUtils.today();
    }

    public void decreaseBalance(Money deposit) {
        super.setBalance(reduceBalanceAccountingForPenalty(getBalance(), deposit, penaltyFee));
    }

    public Money getMinimumBalance() {
        return new Money(minimumAmount, getCurrency());
    }

    public Money getMonthlyMaintenanceFee() {
        return new Money(monthlyMaintenanceFee, getCurrency());
    }

    public int getOverduePeriods(Date lastAccess) {
        Date today = DateUtils.today();
        return DateUtils.getPeriodBetweenDates(lastAccess, today).getMonths()
                + (DateUtils.getPeriodBetweenDates(lastAccess, today).getYears() * 12);
    }

    public void chargeMaintenanceIfApplies(Date lastAccess) {
        int overduePeriods = getOverduePeriods(lastAccess);
        if (overduePeriods > 0)
            setBalance(new Money(subtractMaintenance(monthlyMaintenanceFee, overduePeriods), getCurrency()));
    }

    @NonNull
    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(@NonNull Date lastAccess) {
        chargeMaintenanceIfApplies(lastAccess);
        this.lastAccess = DateUtils.today();
    }

    @Override
    public String toString() {
        return super.toString()
                + lastAccess.getTime()
                + monthlyMaintenanceFee
                + minimumAmount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CheckingAccount account) {
            return super.equals(obj)
                    && lastAccess.equals(account.getLastAccess())
                    && monthlyMaintenanceFee.equals(account.getMonthlyMaintenanceFee().getAmount())
                    && minimumAmount.equals(account.getMinimumBalance().getAmount());
        }
        return false;
    }
}
