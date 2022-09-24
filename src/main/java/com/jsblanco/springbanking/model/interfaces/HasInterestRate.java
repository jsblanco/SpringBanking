package com.jsblanco.springbanking.model.interfaces;

import com.jsblanco.springbanking.model.util.Money;

import java.math.BigDecimal;

public interface HasInterestRate {
    public Money getInterestRate();
    public void setInterestRate(BigDecimal interestRate);
}
