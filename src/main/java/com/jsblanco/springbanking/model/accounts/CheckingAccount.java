package com.jsblanco.springbanking.model.accounts;

import com.jsblanco.springbanking.model.interfaces.HasMinimumBalance;
import com.jsblanco.springbanking.model.interfaces.RequiresMaintenance;
import com.jsblanco.springbanking.model.util.Money;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Entity
public class CheckingAccount extends Account implements RequiresMaintenance, HasMinimumBalance {
    @NonNull
    private BigDecimal minimumBalance;
    @NonNull
    private BigDecimal monthlyMaintenanceFee;

    public Money getMinimumBalance() {
        return new Money(minimumBalance, getCurrency());
    }

    public void setMinimumBalance(Money minimumBalance) {
        if (getCurrency() != minimumBalance.getCurrency())
            throw new IllegalArgumentException("This account uses " + getCurrency() + " but you tried to input " + minimumBalance.getCurrency() + ".");
        this.minimumBalance = minimumBalance.getAmount();
    }

    public Money getMonthlyMaintenanceFee() {
        return new Money(monthlyMaintenanceFee, getCurrency());
    }

    public void setMonthlyMaintenanceFee(Money monthlyMaintenanceFee) {
        if (getCurrency() != monthlyMaintenanceFee.getCurrency())
            throw new IllegalArgumentException("This account uses " + getCurrency() + " but you tried to input " + monthlyMaintenanceFee.getCurrency() + ".");
        this.monthlyMaintenanceFee = monthlyMaintenanceFee.getAmount();
    }
}
