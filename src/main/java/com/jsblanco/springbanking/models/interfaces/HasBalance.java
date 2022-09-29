package com.jsblanco.springbanking.models.interfaces;

import com.jsblanco.springbanking.models.util.Money;

public interface HasBalance {
    Money getBalance();
    void setBalance(Money balance);
}
