package com.jsblanco.springbanking.models.interfaces;

import com.jsblanco.springbanking.models.util.Money;

import java.math.BigDecimal;

public interface HasMinimumBalance {
    Money getMinimumBalance();
    default Money reduceBalanceAccountingForPenalty(Money balance, Money subtractedAmount, BigDecimal penaltyFee) {
        balance.decreaseAmount(subtractedAmount);
        if (balance.getAmount().compareTo(getMinimumBalance().getAmount()) < 0)
            balance.decreaseAmount(penaltyFee);

        return balance;
    }
}
