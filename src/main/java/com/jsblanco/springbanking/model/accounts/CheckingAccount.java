package com.jsblanco.springbanking.model.accounts;

import com.jsblanco.springbanking.model.interfaces.HasMinimumBalance;
import com.jsblanco.springbanking.model.users.AccountHolder;
import com.jsblanco.springbanking.model.util.Money;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Entity
@DiscriminatorValue("checking_account")
public class CheckingAccount extends Account implements HasMinimumBalance {
    @NonNull
    private BigDecimal minimumBalance;
    @NonNull
    private BigDecimal monthlyMaintenanceFee;

    public static Account createNew(AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        if (primaryOwner == null) throw new IllegalArgumentException("Primary owner cannot be null");
        if (Period.between(primaryOwner.getBirthDay().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                , new Date().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
        ).getYears() < 24) return new StudentCheckingAccount();
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setPrimaryOwner(primaryOwner);
        checkingAccount.setSecondaryOwner(secondaryOwner);
        return checkingAccount;
    }

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
    public void extractMoney(Money deposit) {
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
