package com.jsblanco.springbanking.model.accounts;

import com.jsblanco.springbanking.model.interfaces.HasInterestRate;
import com.jsblanco.springbanking.model.interfaces.HasMinimumBalance;
import com.jsblanco.springbanking.model.util.Money;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("savings_account")
public class SavingsAccount extends Account implements HasInterestRate, HasMinimumBalance {

    @NonNull
    private BigDecimal interestRate;
    @NonNull
    private BigDecimal minimumBalance;

    @Transient
    BigDecimal maxInterestRate = new BigDecimal("0.5");
    @Transient
    BigDecimal minInterestRate = new BigDecimal("0");
    @Transient
    BigDecimal minMinimumAccount = new BigDecimal("100");

    public SavingsAccount() {
        setMinimumAmount(new BigDecimal(1000));
        setInterestRate(new BigDecimal("0.0025"));
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

    @Override
    public void setMinimumAmount(BigDecimal minimumAmount) {
        if (interestRate.compareTo(minMinimumAccount) < 0)
            throw new IllegalArgumentException("Minimum balance fall beneath " + minMinimumAccount);
        super.setMinimumAmount(minimumAmount);
    }

    public Money getInterestRate() {
        return new Money(interestRate, getCurrency());
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate.compareTo(maxInterestRate) > 0)
            throw new IllegalArgumentException("The maximum interest rate for savings accounts is " + maxInterestRate);
        if (interestRate.compareTo(minInterestRate) < 0)
            throw new IllegalArgumentException("Interest rate cannot fall under " + minInterestRate);
        this.interestRate = interestRate;
    }
}
