package com.jsblanco.springbanking.model.interfaces;

import com.jsblanco.springbanking.model.util.Money;

public interface RequiresMaintenance {
    public Money getMonthlyMaintenanceFee();
    public void setMonthlyMaintenanceFee(Money monthlyMaintenanceFee);
}
