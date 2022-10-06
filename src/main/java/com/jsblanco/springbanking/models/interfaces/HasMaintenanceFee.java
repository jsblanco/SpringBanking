package com.jsblanco.springbanking.models.interfaces;

import com.jsblanco.springbanking.models.util.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public interface HasMaintenanceFee extends HasPeriodicChanges, HasBalance {
    Money getMonthlyMaintenanceFee();
    default BigDecimal subtractMaintenance(BigDecimal maintenanceFee, int overduePeriods) {
        BigDecimal maintenance = getBalance().getAmount();
        for (int i = 0; i < overduePeriods; i++)
            maintenance = maintenance.subtract(maintenanceFee);
        return maintenance.setScale(2, RoundingMode.HALF_EVEN);
    }
}
