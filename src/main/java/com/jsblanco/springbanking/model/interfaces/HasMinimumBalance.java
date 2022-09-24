package com.jsblanco.springbanking.model.interfaces;

import com.jsblanco.springbanking.model.util.Money;

public interface HasMinimumBalance {
    public Money getMinimumBalance();
    public Money applyPenaltyIfNewBalanceIsBelowMinimum(Money substractedAmount);
    public void setMinimumBalance(Money minimumBalance);
}
