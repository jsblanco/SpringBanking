package com.jsblanco.springbanking.models.interfaces;

import com.jsblanco.springbanking.models.util.Money;

import java.math.BigDecimal;

public interface HasMinimumBalance {
    Money getMinimumBalance();
    void setMinimumBalance(Money minimumBalance);
    BigDecimal getMinimumAmount();
    void setMinimumAmount(BigDecimal amount);
    default Money reduceBalanceAccountingForPenalty(Money balance, Money subtractedAmount, BigDecimal penaltyFee) {
        balance.decreaseAmount(subtractedAmount);
        if (balance.getAmount().compareTo(getMinimumAmount()) < 0)
            balance.decreaseAmount(penaltyFee);

        return balance;
    }
}
