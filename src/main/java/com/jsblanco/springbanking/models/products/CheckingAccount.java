package com.jsblanco.springbanking.models.products;

import com.jsblanco.springbanking.models.interfaces.HasMaintenanceFee;
import com.jsblanco.springbanking.models.interfaces.HasMinimumBalance;
import com.jsblanco.springbanking.models.util.DateUtils;
import com.jsblanco.springbanking.models.util.Money;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@DiscriminatorValue("checking_account")
public class CheckingAccount extends Account implements HasMinimumBalance, HasMaintenanceFee {
    @NonNull
    private BigDecimal minimumAmount;
    @NonNull
    private BigDecimal monthlyMaintenanceFee;
    @NonNull
    private Date lastAccess;

    public void decreaseBalance(Money deposit) {
        super.setBalance(reduceBalanceAccountingForPenalty(getBalance(), deposit, penaltyFee));
    }

    public Money getMinimumBalance() {
        return new Money(minimumAmount, getCurrency());
    }

    public void setMinimumBalance(Money balance) {
        checkCurrency(balance.getCurrency());
        setMinimumAmount(balance.getAmount());
    }

    public BigDecimal getMinimumAmount() {
        return this.minimumAmount;
    }

    public void setMinimumAmount(BigDecimal amount) {
        this.minimumAmount = amount;
    }

    public Money getMonthlyMaintenanceFee() {
        return new Money(monthlyMaintenanceFee, getCurrency());
    }

    public void setMonthlyMaintenanceFee(Money monthlyMaintenanceFee) {
        checkCurrency(monthlyMaintenanceFee.getCurrency());
        this.monthlyMaintenanceFee = monthlyMaintenanceFee.getAmount();
    }

    public int getOverduePeriods(Date lastAccess) {
        Date today = DateUtils.today();
        return DateUtils.getPeriodBetweenDates(lastAccess, today).getMonths()
                + (DateUtils.getPeriodBetweenDates(lastAccess, today).getYears() * 12);
    }

    public void chargeMaintenanceIfApplies(Date lastAccess) {
        int overduePeriods = getOverduePeriods(lastAccess);
        if (overduePeriods > 0)
            setBalance(new Money(subtractMaintenance(getAmount(), monthlyMaintenanceFee, overduePeriods), getCurrency()));
    }

    @NonNull
    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(@NonNull Date lastAccess) {
        chargeMaintenanceIfApplies(lastAccess);
        this.lastAccess = DateUtils.today();
    }

    public void setMonthlyMaintenanceFee(@NonNull BigDecimal monthlyMaintenanceFee) {
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
    }
}
