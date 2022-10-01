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
    private Date lastAccess;

    @Transient
    private final BigDecimal monthlyMaintenanceFee = new BigDecimal(12);
    @Transient
    private final BigDecimal minimumAmount = new BigDecimal(250);

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
}
