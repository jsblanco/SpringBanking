package com.jsblanco.springbanking.models.interfaces;

import com.jsblanco.springbanking.models.util.Money;

import java.math.BigDecimal;

public interface HasInterestRate {
    public BigDecimal getInterestRate();
    public void setInterestRate(BigDecimal interestRate);
    public void mustInterestBeCharged();
    public Money calculateInterest(int overduePeriods);

}
