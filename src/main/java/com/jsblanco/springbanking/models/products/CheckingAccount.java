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
    private BigDecimal minimumBalance;
    @NonNull
    private BigDecimal monthlyMaintenanceFee;
    @NonNull
    private Date lastAccess;

    public Money getMinimumBalance() {
        return new Money(minimumBalance, getCurrency());
    }

    @Override
    public void decreaseBalance(Money deposit) {
        super.setBalance(applyPenaltyIfNewBalanceIsBelowMinimum(deposit));
    }

    @Override
    public Money applyPenaltyIfNewBalanceIsBelowMinimum(Money substractedAmount) {
        Money finalBalance = getBalance();
        finalBalance.decreaseAmount(substractedAmount);
        if (finalBalance.getAmount().compareTo(getMinimumAmount()) < 0)
            finalBalance.decreaseAmount(getPenaltyFee());

        return finalBalance;
    }

    public void setMinimumBalance(Money minimumBalance) {
        checkCurrency(minimumBalance.getCurrency());
        this.minimumBalance = minimumBalance.getAmount();
    }

    public Money getMonthlyMaintenanceFee() {
        return new Money(monthlyMaintenanceFee, getCurrency());
    }

    public void setMonthlyMaintenanceFee(Money monthlyMaintenanceFee) {
        checkCurrency(monthlyMaintenanceFee.getCurrency());
        this.monthlyMaintenanceFee = monthlyMaintenanceFee.getAmount();
    }


    @Override
    public int getOverduePeriods(Date lastAccess) {
        Date today = DateUtils.today();
        return DateUtils.getPeriodBetweenDates(lastAccess, today).getMonths();
    }

    @Override
    public void chargeMaintenanceIfApplies(Date lastAccess) {
        int overduePeriods = getOverduePeriods(lastAccess);
        if (overduePeriods > 0)
            setBalance(new Money(HasMaintenanceFee.subtractMaintenance(getAmount(), monthlyMaintenanceFee, overduePeriods), getCurrency()));
    }

    @NonNull
    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(@NonNull Date lastAccess) {
        chargeMaintenanceIfApplies(lastAccess);
        this.lastAccess = DateUtils.today();
    }
}
