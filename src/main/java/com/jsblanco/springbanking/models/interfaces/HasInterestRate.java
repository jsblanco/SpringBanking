package com.jsblanco.springbanking.models.interfaces;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public interface HasInterestRate extends HasPeriodicCosts {
    BigDecimal getInterestRate();
    void setInterestRate(BigDecimal interestRate);
    void chargeInterestIfApplies(Date lastAccess);

    static BigDecimal subtractInterest(BigDecimal amount, BigDecimal interestRate, int overduePeriods) {
        BigDecimal balance = new BigDecimal(amount.toString());
        for (int i = 0; i < overduePeriods; i++)
            balance = balance.subtract(balance.multiply(interestRate));
        return balance.setScale(2, RoundingMode.HALF_EVEN);
    }
}
