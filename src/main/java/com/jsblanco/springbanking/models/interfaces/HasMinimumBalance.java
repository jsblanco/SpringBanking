package com.jsblanco.springbanking.models.interfaces;

import com.jsblanco.springbanking.models.util.Money;

public interface HasMinimumBalance {
    public Money getMinimumBalance();
    public Money applyPenaltyIfNewBalanceIsBelowMinimum(Money substractedAmount);
    public void setMinimumBalance(Money minimumBalance);
}
