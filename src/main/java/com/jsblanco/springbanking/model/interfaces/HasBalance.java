package com.jsblanco.springbanking.model.interfaces;

import com.jsblanco.springbanking.model.util.Money;

public interface HasBalance {
    public Money getBalance();
    public void setBalance(Money balance);
}
