package com.jsblanco.springbanking.model.interfaces;

import com.jsblanco.springbanking.model.util.Money;

public interface HasInterestRate {
    public Money getInterestRate();
    public void setInterestRate(Money interestRate);
}
