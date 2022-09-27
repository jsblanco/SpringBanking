package com.jsblanco.springbanking.models.products;

import com.jsblanco.springbanking.models.interfaces.HasMinimumBalance;
import com.jsblanco.springbanking.models.util.Money;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("checking_account")
public class CheckingAccount extends Account implements HasMinimumBalance {
    @NonNull
    private BigDecimal minimumBalance;
    @NonNull
    private BigDecimal monthlyMaintenanceFee;

    public Money getMinimumBalance() {
        return new Money(minimumBalance, getCurrency());
    }

    @Override
    public Money applyPenaltyIfNewBalanceIsBelowMinimum(Money substractedAmount) {
        Money finalBalance = getBalance();
        finalBalance.decreaseAmount(substractedAmount);
        if (finalBalance.getAmount().compareTo(getMinimumAmount()) < 0)
            finalBalance.decreaseAmount(getPenaltyFee());

        return finalBalance;
    }

    @Override
    public void decreaseBalance(Money deposit) {
        super.setBalance(applyPenaltyIfNewBalanceIsBelowMinimum(deposit));
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
